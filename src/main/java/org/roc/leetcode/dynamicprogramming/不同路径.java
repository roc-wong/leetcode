package org.roc.leetcode.dynamicprogramming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://leetcode.cn/leetbook/read/path-problems-in-dynamic-programming/rtwu06/
 *
 * @author roc
 * @version 不同路径.java, v 0.1 2024/10/9 20:59
 */
public class 不同路径 {

    public static final Logger LOGGER = LoggerFactory.getLogger(不同路径.class);

    public static void main(String[] args) {
        System.out.println(uniquePaths(3, 2));
    }

    public static int uniquePaths(int m, int n) {
        int[][] f = new int[m][n];
        f[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > 0 && j > 0) {
                    f[i][j] = f[i - 1][j] + f[i][j - 1];
                } else if (i > 0) {
                    f[i][j] = f[i - 1][j];
                } else if (j > 0) {
                    f[i][j] = f[i][j - 1];
                }
            }
        }
        return f[m - 1][n - 1];
    }
}
