package com.galewings.util;

import java.util.ArrayList;
import java.util.List;

public class WuDiffLines {
    // 差分操作の種類
    public enum EditType {MATCH, INSERT, DELETE}

    // 差分結果の1要素
    public static class Diff {
        public final EditType type;
        public final String line;

        Diff(EditType type, String line) {
            this.type = type;
            this.line = line;
        }

        @Override
        public String toString() {
            return type + ": " + line;
        }
    }

    // WuのO(NP)アルゴリズムに基づく行単位差分抽出
    public static List<Diff> diff(String[] a, String[] b) {
        int N = a.length;
        int M = b.length;
        int MAX = N + M;
        int[] v = new int[2 * MAX + 1];
        for (int i = 0; i < v.length; i++) v[i] = -1;
        v[MAX + 1] = 0;

        List<int[]> trace = new ArrayList<>();

        int D;
        for (D = 0; D <= MAX; D++) {
            int[] vCopy = v.clone();
            trace.add(vCopy);

            for (int k = -D; k <= D; k += 2) {
                int index = k + MAX;
                int x;
                if (k == -D || (k != D && v[index - 1] < v[index + 1])) {
                    x = v[index + 1];
                } else {
                    x = v[index - 1] + 1;
                }
                int y = x - k;
                while (x < N && y < M && a[x].trim().equals(b[y].trim())) {
                    x++;
                    y++;
                }
                v[index] = x;
                if (x >= N && y >= M) {
                    return buildDiff(a, b, trace, D, MAX);
                }
            }
        }
        throw new RuntimeException("diff failed");
    }

    // 差分結果を復元
    private static List<Diff> buildDiff(String[] a, String[] b, List<int[]> trace, int D, int MAX) {
        List<Diff> result = new ArrayList<>();
        int x = a.length, y = b.length;

        for (int d = D; d > 0; d--) {
            int[] v = trace.get(d);
            int k = x - y;
            int index = k + MAX;

            int prevK;
            if (k == -d || (k != d && v[index - 1] < v[index + 1])) {
                prevK = k + 1;
            } else {
                prevK = k - 1;
            }
            int prevX = v[prevK + MAX];
            int prevY = prevX - prevK;

            while (x > prevX && y > prevY) {
                result.add(0, new Diff(EditType.MATCH, a[x - 1]));
                x--;
                y--;
            }

            if (x == prevX) {
                result.add(0, new Diff(EditType.INSERT, b[y - 1]));
                y--;
            } else {
                result.add(0, new Diff(EditType.DELETE, a[x - 1]));
                x--;
            }
        }

        while (x > 0 && y > 0) {
            if (a[x - 1].equals(b[y - 1])) {
                result.add(0, new Diff(EditType.MATCH, a[x - 1]));
                x--;
                y--;
            } else {
                break;
            }
        }
        while (x > 0) {
            result.add(0, new Diff(EditType.DELETE, a[x - 1]));
            x--;
        }
        while (y > 0) {
            result.add(0, new Diff(EditType.INSERT, b[y - 1]));
            y--;
        }

        return result;
    }
    
}
