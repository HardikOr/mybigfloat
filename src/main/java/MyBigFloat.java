import java.util.Arrays;

import static java.lang.Math.*;

public class MyBigFloat {
    private int[] integer;
    private int[] decimal;

    private int intlen;
    private int declen;

    private int positive;

    public MyBigFloat(int number) {
        this.positive = number >= 0 ? 1 : -1;
        this.declen = 1;
        this.decimal = new int[]{0};

        int n = abs(number);
        this.intlen = n == 0 ? 1 : (int) (log10(n) + 1);
        this.integer = new int[this.intlen];
        for (int i = 0; i < this.intlen; i++) {
            this.integer[this.intlen - i - 1] = n % 10;
            n /= 10;
        }
    }

    public MyBigFloat(float number) {
        this.positive = number >= 0 ? 1 : -1;

        String[] str = Float.toString(abs(number)).split("[.]");
        this.intlen = str[0].length();
        this.declen = str[1].length();

        this.integer = new int[this.intlen];
        this.decimal = new int[this.declen];

        int pos = 0;
        for (char i : str[0].toCharArray()) {
            this.integer[pos++] = i - '0';
        }

        pos = 0;
        for (char i : str[1].toCharArray()) {
            this.decimal[pos++] = i - '0';
        }
    }

    public MyBigFloat(String string) {
        String s;
        if (string.contains("-")) {
            this.positive = -1;
            s = string.replace("-", "");
        } else {
            this.positive = 1;
            s = string;
        }

        String[] str = s.split("[.]");

        this.intlen = str[0].length();
        this.integer = new int[this.intlen];

        int pos = 0;
        for (char i : str[0].toCharArray()) {
            this.integer[pos++] = i - '0';
        }

        if (str.length > 1) {
            this.declen = str[1].length();
            this.decimal = new int[this.declen];

            pos = 0;
            for (char i : str[1].toCharArray()) {
                this.decimal[pos++] = i - '0';
            }
        } else {
            this.declen = 1;
            this.decimal = new int[]{0};
        }
    }

    private MyBigFloat revert() {
        this.positive = 0 - this.positive;
        return this;
    }

    private MyBigFloat(int[] integer, int[] decimal, int intlen, int declen, int positive) {
        int len = 0;
        while (integer[len] == 0 && len != intlen - 1) {
            len++;
        }
        if (len == 0) {
            this.intlen = intlen;
            this.integer = integer;
        } else {
            this.intlen = intlen - len;
            this.integer = new int[this.intlen];
            System.arraycopy(integer, len, this.integer, 0, this.intlen);
        }

        len = 0;
        while (decimal[declen - len - 1] == 0 && len != declen - 1) {
            len++;
        }
        if (len == 0) {
            this.declen = declen;
            this.decimal = decimal;
        } else {
            this.declen = declen - len;
            this.decimal = new int[this.declen];
            System.arraycopy(decimal, 0, this.decimal, 0, this.declen);
        }

        this.positive = positive;
    }

    public MyBigFloat plus(MyBigFloat other) {
        if (this.positive == -1) {
            if (other.positive == 1)
                return other.minus(this.revert());
            else
                return other.revert().plus(other.revert()).revert();
        } else {
            if (other.positive == -1)
                return this.minus(other.revert());
        }

        int len1 = max(this.declen, other.declen);
//        System.out.println("lendec: " + len1);
        int[] deci = new int[len1];

        int add = 0;
        int num;
        for (int i = len1 - 1; i >= 0; i--) {
            num = add + (i < this.declen ? this.decimal[i] : 0) + (i < other.declen ? other.decimal[i] : 0);
//            System.out.println("n: " + num);
            add = num > 9 ? 1 : 0;
            deci[i] = num % 10;
        }

        int len2 = max(this.intlen, other.intlen) + 1;
//        System.out.println("lenint: " + len2);
        int[] inte = new int[len2];

//        System.out.println("add: " + add);
        for (int i = 0; i < len2; i++) {
            num = add + (this.intlen - i > 0 ? this.integer[this.intlen - i - 1] : 0) + (other.intlen - i > 0 ? other.integer[other.intlen - i - 1] : 0);
            System.out.println("n: " + num);
            add = num > 9 ? 1 : 0;
            inte[len2 - i - 1] = num % 10;
        }

        return new MyBigFloat(inte, deci, len2, len1, 1);
    }

    public static int compare(MyBigFloat a, MyBigFloat b) {
        if (a.intlen > b.intlen)
            return 1;
        else if (a.intlen < b.intlen)
            return -1;

        for (int i = 0; i < a.intlen; i++) {
            if (a.integer[i] > b.integer[i])
                return 1;
            else if (a.integer[i] < b.integer[i])
                return -1;
        }

        for (int i = 0; i < min(a.declen, b.declen); i++) {
            if (a.decimal[i] > b.decimal[i])
                return 1;
            else if (a.decimal[i] < b.decimal[i])
                return -1;
        }

        return 0;
    }

    public MyBigFloat minus(MyBigFloat other) {
        if (this.positive == -1) {
            if (other.positive == 1)
                return this.revert().plus(other).revert();
            else
                return other.revert().minus(this.revert());
        } else {
            if (other.positive == -1)
                return this.plus(other.revert());
        }

        if (MyBigFloat.compare(this, other) < 0)
            return other.minus(this).revert();

        int len1 = max(this.declen, other.declen);
//        System.out.println("lendec: " + len1);
        int[] deci = new int[len1];

        int add = 0;
        int num;
        for (int i = len1 - 1; i >= 0; i--) {
            num = 10 - add + (i < this.declen ? this.decimal[i] : 0) - (i < other.declen ? other.decimal[i] : 0);
            add = num < 10 ? 1 : 0;
            deci[i] = num % 10;
        }

        int len2 = max(this.intlen, other.intlen);
//        System.out.println("lenint: " + len2);
        int[] inte = new int[len2];

//        System.out.println("add: " + add);
        for (int i = 0; i < len2; i++) {
            num = 10 - add + (this.intlen - i > 0 ? this.integer[this.intlen - i - 1] : 0) - (other.intlen - i > 0 ? other.integer[other.intlen - i - 1] : 0);
            add = num < 10 ? 1 : 0;
            inte[len2 - i - 1] = num % 10;
        }

        return new MyBigFloat(inte, deci, len2, len1, 1);
    }

    public MyBigFloat multiply(MyBigFloat other) {
        int len1 = this.intlen + this.declen;
        int len2 = other.intlen + other.declen;
        int[] ans = new int[len1 + len2];

        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int num = i + j + 1;
                ans[num] += (i < this.intlen ? this.integer[i] : this.decimal[i - this.intlen]) * (j < other.intlen ? other.integer[j] : other.decimal[j - other.intlen]);
                if (num != 0) ans[num - 1] += ans[num] / 10;
                ans[num] = ans[num] % 10;
            }
        }

        int pos = this.positive * other.positive;
        if (this.toInt() == 0 || other.toInt() == 0)
            pos = 1;

        return new MyBigFloat(Arrays.copyOfRange(ans, 0, len1), Arrays.copyOfRange(ans, len1, len1 + len2), len1, len2, pos);
    }

    public MyBigFloat round(int precision) {
        int add = 0;
        for (int i = this.declen - 1; i >= precision; i--) {
            add = this.decimal[i] + add > 4 ? 1 : 0;
        }

        int[] i = new int[]{0};
        int[] d = new int[precision];

        if (precision == 0) {
            i[0] = 1;
            d = new int[]{0};
        } else {
            d[precision - 1] = add;
        }

        return (new MyBigFloat(Arrays.copyOf(this.integer, this.intlen), Arrays.copyOf(this.decimal, this.declen), this.intlen, precision, 1)).plus(new MyBigFloat(i, d, 1, precision, 1));
    }

    public long toLong() {
        long ans = 0;

        for (int i = 0; i < this.intlen; i++) {
            ans = ans * 10 + this.integer[i];
        }

        return ans * this.positive;
    }

    public int toInt() {
        return (int) this.toLong();
    }

    public float toFloat() {
        float ans = 0;

        for (int i = 0; i < this.declen; i++) {
            ans = ans / 10 + this.decimal[i];
        }

        return this.toInt() + ans * this.positive;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.positive == 1 ? "" : "-");

        for (int i = 0; i < this.intlen; i++) {
            str.append(this.integer[i]);
            System.out.print(this.integer[i]);
        }

        str.append(".");
//        System.out.print(".");

        for (int i = 0; i < this.declen; i++) {
            str.append(this.decimal[i]);
//            System.out.print(this.decimal[i]);
        }

//        System.out.print("\n");
        return str.toString();
    }
}
