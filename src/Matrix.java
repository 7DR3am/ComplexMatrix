import java.util.Locale;

public class Matrix {
    private final Complex[][] data;
    private final int rows, cols;

    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("Размеры должны быть положительны");
        this.rows = rows;
        this.cols = cols;
        data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = Complex.ZERO;
    }

    public int rows() { return rows; }
    public int cols() { return cols; }
    public Complex get(int i, int j) { return data[i][j]; }
    public void set(int i, int j, Complex v) { data[i][j] = v; }

    public Matrix add(Matrix o) {
        requireSameSize(o);
        Matrix r = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r.data[i][j] = data[i][j].add(o.data[i][j]);
        return r;
    }

    public Matrix multiply(Matrix o) {
        if (cols != o.rows)
            throw new IllegalArgumentException("Число столбцов A не равно числу строк B");
        Matrix r = new Matrix(rows, o.cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < o.cols; j++) {
                Complex sum = Complex.ZERO;
                for (int k = 0; k < cols; k++)
                    sum = sum.add(data[i][k].mul(o.data[k][j]));
                r.data[i][j] = sum;
            }
        return r;
    }

    /** A * B^(-1) */
    public Matrix divide(Matrix o) {
        return multiply(o.inverse());
    }

    public Matrix transpose() {
        Matrix r = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r.data[j][i] = data[i][j];
        return r;
    }

    public Matrix inverse() {
        if (rows != cols) throw new IllegalArgumentException("Матрица должна быть квадратной");
        int n = rows;
        Complex[][] a = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) a[i][j] = data[i][j];
            for (int j = 0; j < n; j++) a[i][n + j] = (i == j) ? Complex.ONE : Complex.ZERO;
        }
        for (int col = 0; col < n; col++) {
            int pivot = -1; double best = 0;
            for (int i = col; i < n; i++) {
                double v = a[i][col].abs();
                if (v > best) { best = v; pivot = i; }
            }
            if (pivot == -1 || best < 1e-12)
                throw new ArithmeticException("Матрица вырождена, обратной не существует");
            Complex[] t = a[col]; a[col] = a[pivot]; a[pivot] = t;

            Complex p = a[col][col];
            for (int j = 0; j < 2 * n; j++) a[col][j] = a[col][j].div(p);

            for (int i = 0; i < n; i++) {
                if (i == col) continue;
                Complex f = a[i][col];
                if (f.isZero()) continue;
                for (int j = 0; j < 2 * n; j++)
                    a[i][j] = a[i][j].sub(f.mul(a[col][j]));
            }
        }
        Matrix r = new Matrix(n, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                r.data[i][j] = a[i][n + j];
        return r;
    }

    public Complex determinant() {
        if (rows != cols) throw new IllegalArgumentException("Матрица должна быть квадратной");
        int n = rows;
        Complex[][] m = new Complex[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = data[i][j];

        Complex det = Complex.ONE;
        for (int col = 0; col < n; col++) {
            int pivot = -1; double best = 0;
            for (int i = col; i < n; i++) {
                double v = m[i][col].abs();
                if (v > best) { best = v; pivot = i; }
            }
            if (pivot == -1 || best < 1e-12) return Complex.ZERO;
            if (pivot != col) {
                Complex[] t = m[col]; m[col] = m[pivot]; m[pivot] = t;
                det = det.mul(new Complex(-1, 0));
            }
            det = det.mul(m[col][col]);
            for (int i = col + 1; i < n; i++) {
                Complex f = m[i][col].div(m[col][col]);
                for (int j = col; j < n; j++)
                    m[i][j] = m[i][j].sub(f.mul(m[col][j]));
            }
        }
        return det;
    }

    private void requireSameSize(Matrix o) {
        if (rows != o.rows || cols != o.cols)
            throw new IllegalArgumentException("Размеры матриц не совпадают");
    }

    @Override
    public String toString() {
        int width = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                width = Math.max(width, data[i][j].toString().length());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format(Locale.US, "%-" + (width + 2) + "s", data[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}