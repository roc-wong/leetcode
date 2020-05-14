package org.roc.leetcode.str;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roc
 * @since 2020/5/12 9:41
 */
public class SplitStr {

    public static void main(String[] args) {
        String document =
                "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
                        "算法可以宽泛的分为三类，\n" +
                        "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
                        "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
                        "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
        document = "voice:语音95456.amr,localurl:/storage/emulated/0/Android/data/com.kylin.gradle.immodule.debug/1100200501181368#immodule/files/q/c/96cb18da-93f3-11ea-b12f-d71914d1fc0c.amr,remoteurl:https://a1.easemob.com/1100200501181368/immodule/chatfiles/96cb18da-93f3-11ea-b12f-d71914d1fc0c,length:0";
//        List<String> strings = spiltSentence(document);
        List<Entry> strings = spilt(document);

        strings.forEach(entry -> {
            System.out.println(entry.key + " " + entry.value);
        });
    }

    /**
     * 将文章分割为句子
     */
    static List<String> spiltSentence(String document) {
        List<String> sentences = new ArrayList<String>();
        if (document == null) {
            return sentences;
        }
        for (String line : document.split("[\r\n]")) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            for (String sent : line.split("[:,]")) {
                sent = sent.trim();
                if (sent.length() == 0) {
                    continue;
                }
                sentences.add(sent);
            }
        }

        return sentences;
    }


    static List<Entry> spilt(String document) {
        List<Entry> sentences = new ArrayList<>();
        if (document == null) {
            return sentences;
        }
        for (String line : document.split(",")) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            int colonIndex = line.indexOf(":");
            String key = line.substring(0, colonIndex);
            String value = line.substring(colonIndex + 1);
            sentences.add(new Entry(key, value));
        }
        return sentences;
    }

}

class Entry {

    String key;
    String value;

    public Entry(String key, String value) {
        this.key = key;
        this.value = value;
    }
}