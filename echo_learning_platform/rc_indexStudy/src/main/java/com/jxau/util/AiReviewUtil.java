package com.jxau.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * AI content review utility
 * Uses DeepSeek API to check articles for ads or spam
 */
public class AiReviewUtil {

    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    private static final String API_KEY = "your-deepseek-api-key";

    public static class ReviewResult {
        public boolean passed;
        public String reason;

        public ReviewResult(boolean passed, String reason) {
            this.passed = passed;
            this.reason = reason;
        }
    }

    public static ReviewResult reviewContent(String title, String content) {
        if (API_KEY.startsWith("your-")) {
            return localReview(title, content);
        }
        return aiReview(title, content);
    }

    private static ReviewResult aiReview(String title, String content) {
        try {
            String truncated = content;
            if (truncated != null && truncated.length() > 2000) {
                truncated = truncated.substring(0, 2000);
            }
            String prompt = "Review this article for: ads, spam, meaningless content, illegal content. "
                    + "Title: " + title + " Content: " + truncated
                    + " Reply JSON only: {\"passed\": true/false, \"reason\": \"review opinion in Chinese\"}";

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("model", "deepseek-chat");
            body.put("temperature", 0.1);

            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", prompt);
            JSONArray msgs = new JSONArray();
            msgs.add(msg);
            body.put("messages", msgs);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toJSONString().getBytes("UTF-8"));
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8").useDelimiter("\\A")) {
                String response = scanner.hasNext() ? scanner.next() : "";
                JSONObject resp = JSON.parseObject(response);
                String aiText = resp.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                int braceStart = aiText.indexOf('{');
                int braceEnd = aiText.lastIndexOf('}') + 1;
                if (braceStart >= 0 && braceEnd > braceStart) {
                    JSONObject result = JSON.parseObject(aiText.substring(braceStart, braceEnd));
                    return new ReviewResult(
                            result.getBooleanValue("passed"),
                            result.getString("reason"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ReviewResult(true, "AI review unavailable, passed by default");
    }

    private static ReviewResult localReview(String title, String content) {
        String full = (title + " " + (content != null ? content : "")).toLowerCase();

        if (content == null || content.trim().length() < 10) {
            return new ReviewResult(false, "Content too short, minimum 10 characters required");
        }

        if (full.matches("^(test|asdf|hello|aaa)+.*")) {
            return new ReviewResult(false, "Please do not post meaningless test content");
        }

        String[] adWords = {"wechat", "qq", "free gift", "scan qr", "agent", "make money", "work from home"};
        for (String word : adWords) {
            if (full.contains(word)) {
                return new ReviewResult(false, "Content appears to contain advertisement: " + word);
            }
        }

        return new ReviewResult(true, "Review passed");
    }
}
