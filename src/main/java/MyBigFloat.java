import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.*;

public class MyBigFloat {
    private int[] integer;
    private int[] decimal;

    private int intlen;
    private int declen;

    private int sign;

    public MyBigFloat(int number) {
        this(new MyBigFloat(Integer.toString(number)));
    }

    public MyBigFloat(float number) {
        this(new MyBigFloat(Float.toString(number)));
    }

    public MyBigFloat(String string) {
        if (string == null || string.isEmpty()) throw new IllegalArgumentException("String is null || empty");
        if (!string.matches("[-]?[\\d]+[.]?[\\d]*[E]?[-]?[\\d]*")) throw new NumberFormatException("Illegal symbols");

        String[] str;
        int add = 0;

        if (string.contains("E")) {
            str = string.split("[E]");
            str[0] = str[0].replace(".", "");
            add = Integer.parseInt(str[1]);
        } else {
            if (string.contains(".")) {
                str = string.split("[.]");
            } else {
                str = new String[2];
                str[0] = string;
                str[1] = "0";
            }
        }

        if (str[0].contains("-")) {
            this.sign = -1;
            str[0] = str[0].replace("-", "");
        } else {
            this.sign = 1;
        }

        if (add > 0) {
            this.intlen = add + 1;
            this.integer = new int[this.intlen];
            this.declen = 1;
            this.decimal = new int[this.declen];

            int pos = 0;
            for (char i : str[0].toCharArray()) {
                this.integer[pos++] = i - '0';
            }
        } else if (add < 0) {
            this.intlen = 1;
            this.integer = new int[this.intlen];
            this.declen = str[0].length() - 1 - add;
            this.decimal = new int[this.declen];

            System.out.println(intlen);
            System.out.println(declen);

            int pos = 0;
            for (char i : str[0].toCharArray()) {
                this.decimal[this.declen - str[0].length() + pos++] = i - '0';
            }
        } else {
            this.intlen = str[0].length();
            this.integer = new int[this.intlen];

            int pos = 0;
            for (char i : str[0].toCharArray()) {
                this.integer[pos++] = i - '0';
            }

            this.declen = str[1].length();
            this.decimal = new int[this.declen];

            pos = 0;
            for (char i : str[1].toCharArray()) {
                this.decimal[pos++] = i - '0';
            }
        }
    }

    private MyBigFloat inverse() {
        this.sign = -this.sign;
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

        this.sign = positive;
    }

    private MyBigFloat(MyBigFloat other) {
        this.integer = other.integer;
        this.decimal = other.decimal;
        this.intlen = other.intlen;
        this.declen = other.declen;
        this.sign = other.sign;
    }

    public MyBigFloat plus(MyBigFloat other) {
        if (this.sign == -1) {
            if (other.sign == 1)
                return other.minus(this.inverse());
            else
                return other.inverse().plus(other.inverse()).inverse();
        } else {
            if (other.sign == -1)
                return this.minus(other.inverse());
        }

        int len1 = max(this.declen, other.declen);
        int[] deci = new int[len1];

        int add = 0;
        int num;
        for (int i = len1 - 1; i >= 0; i--) {
            num = add + (i < this.declen ? this.decimal[i] : 0) + (i < other.declen ? other.decimal[i] : 0);
            add = num > 9 ? 1 : 0;
            deci[i] = num % 10;
        }

        int len2 = max(this.intlen, other.intlen) + 1;
        int[] inte = new int[len2];

        for (int i = 0; i < len2; i++) {
            num = add + (this.intlen - i > 0 ? this.integer[this.intlen - i - 1] : 0) + (other.intlen - i > 0 ? other.integer[other.intlen - i - 1] : 0);
            add = num > 9 ? 1 : 0;
            inte[len2 - i - 1] = num % 10;
        }

        return new MyBigFloat(inte, deci, len2, len1, 1);
    }

    public static int compare(MyBigFloat a, MyBigFloat b) {
        if (a.sign == 1 && b.sign == -1)
            return a.sign;
        if (a.sign == -1 && b.sign == 1)
            return a.sign;

        if (a.intlen > b.intlen)
            return a.sign;
        else if (a.intlen < b.intlen)
            return -a.sign;

        for (int i = 0; i < a.intlen; i++) {
            if (a.integer[i] > b.integer[i])
                return a.sign;
            else if (a.integer[i] < b.integer[i])
                return -a.sign;
        }

        for (int i = 0; i < min(a.declen, b.declen); i++) {
            if (a.decimal[i] > b.decimal[i])
                return a.sign;
            else if (a.decimal[i] < b.decimal[i])
                return -a.sign;
        }

        if (a.declen > b.declen)
            return a.sign;
        else if (a.declen < b.declen)
            return -a.sign;

        return 0;
    }

    public MyBigFloat minus(MyBigFloat other) {
        if (this.sign == -1) {
            if (other.sign == 1)
                return this.inverse().plus(other).inverse();
            else
                return other.inverse().minus(this.inverse());
        } else {
            if (other.sign == -1)
                return this.plus(other.inverse());
        }

        if (MyBigFloat.compare(this, other) < 0)
            return other.minus(this).inverse();

        int len1 = max(this.declen, other.declen);
        int[] deci = new int[len1];

        int add = 0;
        int num;
        for (int i = len1 - 1; i >= 0; i--) {
            num = 10 - add + (i < this.declen ? this.decimal[i] : 0) - (i < other.declen ? other.decimal[i] : 0);
            add = num < 10 ? 1 : 0;
            deci[i] = num % 10;
        }

        int len2 = max(this.intlen, other.intlen);
        int[] inte = new int[len2];

        for (int i = 0; i < len2; i++) {
            num = 10 - add + (this.intlen - i > 0 ? this.integer[this.intlen - i - 1] : 0) - (other.intlen - i > 0 ? other.integer[other.intlen - i - 1] : 0);
            add = num < 10 ? 1 : 0;
            inte[len2 - i - 1] = num % 10;
        }

        return new MyBigFloat(inte, deci, len2, len1, 1);
    }

    public MyBigFloat multiply(MyBigFloat other) {
        int len1 = this.intlen + other.intlen;
        int len2 = this.declen + other.declen;
        int[] ans = new int[len1 + len2];

        for (int i = this.intlen + this.declen - 1; i >= 0; i--) {
            for (int j = other.intlen + other.declen - 1; j >= 0; j--) {
                int num = i + j + 1;
                ans[num] += (i < this.intlen ? this.integer[i] : this.decimal[i - this.intlen]) * (j < other.intlen ? other.integer[j] : other.decimal[j - other.intlen]);
                if (num != 0) ans[num - 1] += ans[num] / 10;
                ans[num] = ans[num] % 10;
            }
        }

        int pos = this.sign * other.sign;
        if (this.intlen == 1 && this.integer[0] == 0 && this.declen == 1 && this.decimal[0] == 0 ||
                other.intlen == 1 && other.integer[0] == 0 && other.declen == 1 && other.decimal[0] == 0)
            pos = 1;

        return new MyBigFloat(Arrays.copyOfRange(ans, 0, len1), Arrays.copyOfRange(ans, len1, len1 + len2), len1, len2, pos);
    }

    public MyBigFloat round(int precision) {
        int add = 0;
        for (int i = this.declen - 1; i >= precision; i--) {
            add = this.decimal[i] + add > 4 ? 1 : 0;
        }

        int[] i = new int[]{0};
        int[] d;

        if (precision == 0) {
            i[0] = add;
            d = new int[]{0};
            precision = 1;
        } else {
            d = new int[precision];
            d[precision - 1] = add;
        }

        return (new MyBigFloat(Arrays.copyOf(this.integer, this.intlen), Arrays.copyOf(this.decimal, this.declen), this.intlen, precision, 1)).plus(new MyBigFloat(i, d, 1, precision, 1));
    }

    public int toInt() {
        if ((this.sign == 1 && MyBigFloat.compare(this, new MyBigFloat(Integer.MAX_VALUE)) == 1) ||
                (this.sign == -1 && MyBigFloat.compare(this, new MyBigFloat(Integer.MIN_VALUE)) == -1))
            throw new IllegalArgumentException("Number can't be converted to int");

        int ans = 0;

        for (int i = 0; i < this.intlen; i++) {
            ans = ans * 10 + this.integer[i];
        }

        return ans * this.sign;
    }

    public float toFloat() {
        if ((this.sign == 1 && MyBigFloat.compare(this, new MyBigFloat(Float.MAX_VALUE)) == 1) ||
                (this.sign == -1 && MyBigFloat.compare(this, new MyBigFloat(Float.MAX_VALUE).inverse()) == -1))
            throw new IllegalArgumentException("Number can't be converted to float");

        return Float.parseFloat(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(this.sign == 1 ? "" : "-");
        for (int i = 0; i < this.intlen; i++) str.append(this.integer[i]);
        str.append(".");
        for (int i = 0; i < this.declen; i++) str.append(this.decimal[i]);

        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBigFloat that = (MyBigFloat) o;

        return intlen == that.intlen &&
                declen == that.declen &&
                sign == that.sign &&
                Arrays.equals(integer, that.integer) &&
                Arrays.equals(decimal, that.decimal);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(intlen, declen, sign);
        result = 31 * result + Arrays.hashCode(integer);
        result = 31 * result + Arrays.hashCode(decimal);

        return result;
    }
}
