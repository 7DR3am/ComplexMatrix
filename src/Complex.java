public class Complex {
    private final double re;
    private final double im;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex(double re) { this(re, 0); }

    public double re() { return re; }
    public double im() { return im; }

    public Complex add(Complex o) { return new Complex(re + o.re, im + o.im); }
    public Complex sub(Complex o) { return new Complex(re - o.re, im - o.im); }
    public Complex mul(Complex o) {
        return new Complex(re * o.re - im * o.im, re * o.im + im * o.re);
    }
    public Complex div(Complex o) {
        double d = o.re * o.re + o.im * o.im;
        if (d == 0) throw new ArithmeticException("Деление на ноль");
        return new Complex((re * o.re + im * o.im) / d,
                (im * o.re - re * o.im) / d);
    }
    public Complex conjugate() { return new Complex(re, -im); }
    public double abs()        { return Math.sqrt(re * re + im * im); }
    public boolean isZero()    { return re == 0 && im == 0; }

    @Override
    public String toString() {
        if (im == 0) return String.format("%.4f", re);
        if (re == 0) return String.format("%.4fi", im);
        return String.format("%.4f %s %.4fi", re, im < 0 ? "-" : "+", Math.abs(im));
    }
}