#!/bin/bash
set -e

# ==========================================
# echo_learning_platform Docker 一键部署脚本
# 用法: bash deploy.sh [command]
#   build   — 编译 Maven + 构建 Docker 镜像
#   up      — 启动所有容器
#   down    — 停止所有容器
#   restart — 重启所有容器
#   logs    — 查看日志
#   ps      — 查看容器状态
# ==========================================

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DOCKER_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_DIR="${DOCKER_DIR}/app"

# 需要部署的模块列表
MODULES=("rc_log" "rc_personal" "rc_indexStudy" "rc_question" "rc_comment" "rc_webMessage" "rc_manage" "rc_order")

log_info()  { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 检查依赖
check_dependencies() {
    log_info "检查环境依赖..."

    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装！请先安装 Docker"
        log_info "安装命令: yum install -y docker"
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null 2>&1; then
        log_error "Docker Compose 未安装！"
        exit 1
    fi

    log_info "Docker: $(docker --version)"
    log_info "Docker Compose: $(docker compose version 2>/dev/null || docker-compose --version)"

    # 检查 Docker 是否运行
    if ! docker info &> /dev/null; then
        log_error "Docker 服务未运行！请执行: systemctl start docker"
        exit 1
    fi

    log_info "环境检查通过 ✓"
}

# 检查 .env 配置
check_env() {
    if [ ! -f "${DOCKER_DIR}/.env" ]; then
        log_warn ".env 文件不存在，从模板复制..."
        cp "${DOCKER_DIR}/.env.example" "${DOCKER_DIR}/.env" 2>/dev/null || true
        if [ ! -f "${DOCKER_DIR}/.env" ]; then
            log_error "请先创建 docker/.env 文件"
            exit 1
        fi
    fi
    log_info ".env 配置已加载 ✓"
}

# 编译 Maven 项目并准备 JAR
build_maven() {
    log_info "第1步: 编译 Maven 项目..."

    if ! command -v mvn &> /dev/null; then
        log_warn "Maven 未安装，尝试用 Docker Maven 容器编译..."
        docker run --rm \
            -v "${PROJECT_ROOT}:/app" \
            -v "${HOME}/.m2:/root/.m2" \
            -w /app \
            maven:3.8.1-jdk-8 \
            mvn clean package -DskipTests -q
    else
        log_info "使用本地 Maven 编译 (版本: $(mvn --version 2>&1 | head -1))"
        cd "${PROJECT_ROOT}"
        mvn clean package -DskipTests -q
    fi

    if [ $? -ne 0 ]; then
        log_error "Maven 编译失败！"
        log_info "请检查代码错误后重试"
        exit 1
    fi

    log_info "Maven 编译完成 ✓"
}

# 复制 JAR 文件到 app 目录
prepare_jars() {
    log_info "第2步: 准备 JAR 文件..."

    # 清理旧的 app 目录
    rm -rf "${APP_DIR}"
    mkdir -p "${APP_DIR}"

    # 复制每个模块的 JAR
    for module in "${MODULES[@]}"; do
        local jar_path="${PROJECT_ROOT}/${module}/target/${module}-1.0-SNAPSHOT.jar"
        if [ -f "${jar_path}" ]; then
            cp "${jar_path}" "${APP_DIR}/${module}.jar"
            log_info "  ✓ ${module}.jar"
        else
            log_error "  ✗ ${module}.jar 未找到: ${jar_path}"
            exit 1
        fi
    done

    log_info "JAR 文件准备完成 ✓ (${APP_DIR})"
}

# 构建 Docker 镜像
build_images() {
    log_info "第3步: 构建 Docker 镜像..."

    cd "${DOCKER_DIR}"
    docker compose build --no-cache

    if [ $? -ne 0 ]; then
        log_error "Docker 镜像构建失败！"
        exit 1
    fi

    log_info "Docker 镜像构建完成 ✓"
}

# 启动服务
start_services() {
    log_info "第4步: 启动所有容器..."

    cd "${DOCKER_DIR}"
    docker compose up -d

    log_info "容器启动完成 ✓"
    log_info "查看运行状态: cd ${DOCKER_DIR} && docker compose ps"
    log_info "查看实时日志: cd ${DOCKER_DIR} && docker compose logs -f"
}

# 停止服务
stop_services() {
    log_info "停止所有容器..."
    cd "${DOCKER_DIR}"
    docker compose down
    log_info "所有容器已停止 ✓"
}

# 查看状态
show_status() {
    cd "${DOCKER_DIR}"
    echo ""
    echo "========== 容器状态 =========="
    docker compose ps
    echo ""
    echo "========== 资源占用 =========="
    docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"
}

# 查看日志
show_logs() {
    cd "${DOCKER_DIR}"
    if [ -z "$2" ]; then
        docker compose logs -f
    else
        docker compose logs -f "$2"
    fi
}

# 初始化数据库
init_database() {
    log_info "等待 MySQL 初始化..."

    # 等待 MySQL 就绪
    local retries=30
    while [ $retries -gt 0 ]; do
        if docker exec rc-mysql mysqladmin ping -h localhost -u root -p"${MYSQL_ROOT_PASSWORD}" &> /dev/null; then
            log_info "MySQL 就绪 ✓"
            break
        fi
        sleep 3
        retries=$((retries - 1))
    done

    if [ $retries -eq 0 ]; then
        log_error "MySQL 启动超时！"
        log_info "请检查: docker logs rc-mysql"
        exit 1
    fi

    log_info "数据库初始化完成 ✓"
}

# ==========================================
# 主入口
# ==========================================
case "${1:-build}" in
    build)
        check_dependencies
        check_env
        build_maven
        prepare_jars
        build_images
        start_services
        init_database
        log_info "========================================"
        log_info "✅ 部署完成！"
        log_info "Nacos 控制台: http://服务器IP:8848/nacos"
        log_info "服务状态: cd ${DOCKER_DIR} && docker compose ps"
        log_info "========================================"
        ;;
    up)
        check_dependencies
        check_env
        start_services
        init_database
        show_status
        ;;
    down)
        stop_services
        ;;
    restart)
        stop_services
        start_services
        init_database
        show_status
        ;;
    status|ps)
        show_status
        ;;
    logs)
        shift
        show_logs "$@"
        ;;
    rebuild)
        build_images
        start_services
        ;;
    *)
        echo "用法: bash deploy.sh [command]"
        echo ""
        echo "命令:"
        echo "  build   编译 Maven + 构建镜像 + 启动 (默认)"
        echo "  up      仅启动容器"
        echo "  down    停止所有容器"
        echo "  restart 重启所有容器"
        echo "  status  查看容器状态"
        echo "  logs    查看日志 (可指定服务名过滤)"
        echo "  rebuild 仅重新构建镜像并启动 (不编译Maven)"
        echo ""
        echo "示例:"
        echo "  bash deploy.sh               # 完整部署"
        echo "  bash deploy.sh up            # 仅启动"
        echo "  bash deploy.sh down          # 停止"
        echo "  bash deploy.sh logs rc-index # 查看单个服务日志"
        ;;
esac
