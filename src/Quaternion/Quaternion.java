package Quaternion;

import java.util.Objects;

/** Quaternions. Basic operations. */
public class Quaternion {
   /* VIITED:
   https://introcs.cs.princeton.edu/java/32class/Quaternion.java.html
   https://alvinalexander.com/java/jwarehouse/commons-math3-3.6.1/src/main/java/org/apache/commons/math3/complex/Quaternion.java.shtml
   */
   public static final double DELTA = 0.000001;
   private final double a, b, c, d;

   /** Constructor from four double values.
    * @param a real part
    * @param b imaginary part i
    * @param c imaginary part j
    * @param d imaginary part k
    */
   public Quaternion(double a, double b, double c, double d) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
   }

   /** Real part of the quaternion.
    * @return real part
    */
   public double getRpart() {
      return a;
   }

   /** Imaginary part i of the quaternion.
    * @return imaginary part i
    */
   public double getIpart() {
      return b;
   }

   /** Imaginary part j of the quaternion.
    * @return imaginary part j
    */
   public double getJpart() {
      return c;
   }

   /** Imaginary part k of the quaternion.
    * @return imaginary part k
    */
   public double getKpart() {
      return d;
   }

   /** Conversion of the quaternion to the string.
    * @return a string form of this quaternion:
    * "a+bi+cj+dk"
    * (without any brackets)
    */
   @Override
   public String toString() {
      String result = String.format("%s", a);

      result += b < 0 ? "" : "+";
      result += String.format("%si", b);

      result += c < 0 ? "" : "+";
      result += String.format("%sj", c);

      result += d < 0 ? "" : "+";
      result += String.format("%sk", d);

      return result;
   }

   /** Conversion from the string to the quaternion.
    * Reverse to <code>toString</code> method.
    * @throws IllegalArgumentException if string s does not represent
    *     a quaternion (defined by the <code>toString</code> method)
    * @param s string of form produced by the <code>toString</code> method
    * @return a quaternion represented by string s
    */
   public static Quaternion valueOf (String s) {
      if (s.length() == 0) throw new IllegalArgumentException("empty string does not represent a quaternion");
      Double a = null, b = null, c = null, d = null;

      String[] chars = s.split("");

      String num = chars[0];
      String symbol = "";
      int counter = 0;

      for (int i = 1; i < chars.length; i++) {
         if (chars[i].equals("-") || chars[i].equals("+")) {
            if (counter == 0) {
               a = Double.parseDouble(num);
            } else if (counter == 1) {
               b = Double.parseDouble(symbol + num);
            } else if (counter == 2) {
               c = Double.parseDouble(symbol + num);
            }
            counter++;
            num = "";
            symbol = chars[i];

         } else if (!chars[i].equals("i") && !chars[i].equals("j") && !chars[i].equals("k"))  {
            num += chars[i];
         } else if (chars[i].equals("k")) {
            d = Double.parseDouble(symbol + num);
         }
      }

      if (a == null || b == null || c == null || d == null) {
         throw new IllegalArgumentException("given string " + s + " does not represent a quaternion");
      }
      return new Quaternion(a, b, c, d);
   }

   /** Clone of the quaternion.
    * @return independent clone of <code>this</code>
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Quaternion(a, b, c, d);
   }

   /** Test whether the quaternion is zero.
    * @return true, if the real part and all the imaginary parts are (close to) zero
    */
   public boolean isZero() {
      return Math.abs(a) < DELTA && Math.abs(b) < DELTA &&
              Math.abs(c) < DELTA && Math.abs(d) < DELTA;
   }

   /** Conjugate of the quaternion. Expressed by the formula
    *     conjugate(a+bi+cj+dk) = a-bi-cj-dk
    * @return conjugate of <code>this</code>
    */
   public Quaternion conjugate() {
      return new Quaternion(a, -b, -c, -d);
   }

   /** Opposite of the quaternion. Expressed by the formula
    *    opposite(a+bi+cj+dk) = -a-bi-cj-dk
    * @return quaternion <code>-this</code>
    */
   public Quaternion opposite() {
      return new Quaternion(-a, -b, -c, -d);
   }

   /** Sum of quaternions. Expressed by the formula
    *    (a1+b1i+c1j+d1k) + (a2+b2i+c2j+d2k) = (a1+a2) + (b1+b2)i + (c1+c2)j + (d1+d2)k
    * @param q addend
    * @return quaternion <code>this+q</code>
    */
   public Quaternion plus (Quaternion q) {
      Quaternion q0 = this;
      return new Quaternion(q0.a + q.getRpart(), q0.b + q.getIpart(), q0.c + q.getJpart(), q0.d + q.getKpart());
   }

   /** Product of quaternions. Expressed by the formula
    *  (a1+b1i+c1j+d1k) * (a2+b2i+c2j+d2k) = (a1a2-b1b2-c1c2-d1d2) + (a1b2+b1a2+c1d2-d1c2)i +
    *  (a1c2-b1d2+c1a2+d1b2)j + (a1d2+b1c2-c1b2+d1a2)k
    * @param q factor
    * @return quaternion <code>this*q</code>
    */
   public Quaternion times (Quaternion q) {
      Quaternion q0 = this;
      double a2 = q0.a*q.getRpart() - q0.b*q.getIpart() - q0.c*q.getJpart() - q0.d*q.getKpart();
      double b2 = q0.a*q.getIpart() + q0.b*q.getRpart() + q0.c*q.getKpart() - q0.d*q.getJpart();
      double c2 = q0.a*q.getJpart() - q0.b*q.getKpart() + q0.c*q.getRpart() + q0.d*q.getIpart();
      double d2 = q0.a*q.getKpart() + q0.b*q.getJpart() - q0.c*q.getIpart() + q0.d*q.getRpart();
      return new Quaternion(a2, b2, c2, d2);
   }

   /** Multiplication by a coefficient.
    * @param r coefficient
    * @return quaternion <code>this*r</code>
    */
   public Quaternion times (double r) {
      return new Quaternion(a*r, b*r, c*r, d*r);
   }

   /** Inverse of the quaternion. Expressed by the formula
    *     1/(a+bi+cj+dk) = a/(a*a+b*b+c*c+d*d) +
    *     ((-b)/(a*a+b*b+c*c+d*d))i + ((-c)/(a*a+b*b+c*c+d*d))j + ((-d)/(a*a+b*b+c*c+d*d))k
    * @return quaternion <code>1/this</code>
    */
   public Quaternion inverse() {
      double x = a*a + b*b + c*c + d*d;
      if (Math.abs(x) < DELTA) throw new IllegalArgumentException("can not divide by zero");
      return new Quaternion(a/x, -b/x, -c/x, -d/x);
   }

   /** Difference of quaternions. Expressed as addition to the opposite.
    * @param q subtrahend
    * @return quaternion <code>this-q</code>
    */
   public Quaternion minus (Quaternion q) {
      Quaternion q0 = this;
      return new Quaternion(q0.a-q.getRpart(), q0.b-q.getIpart(), q0.c-q.getJpart(), q0.d-q.getKpart());
   }

   /** Right quotient of quaternions. Expressed as multiplication to the inverse.
    * @param q (right) divisor
    * @return quaternion <code>this*inverse(q)</code>
    */
   public Quaternion divideByRight (Quaternion q) {
      double x = a*a + b*b + c*c + d*d;
      if (Math.abs(x) < DELTA) throw new IllegalArgumentException("can not divide by zero");
      return this.times(q.inverse());
   }

   /** Left quotient of quaternions.
    * @param q (left) divisor
    * @return quaternion <code>inverse(q)*this</code>
    */
   public Quaternion divideByLeft (Quaternion q) {
      double x = q.getRpart()*q.getRpart() + q.getIpart()*q.getIpart() + q.getJpart()*q.getJpart() + q.getKpart()*q.getKpart();
      if (Math.abs(x) < DELTA) throw new IllegalArgumentException("can not divide by zero");
      return q.inverse().times(this);
   }

   /** Equality test of quaternions. Difference of equal numbers
    *     is (close to) zero.
    * @param qo second quaternion
    * @return logical value of the expression <code>this.equals(qo)</code>
    */
   @Override
   public boolean equals (Object qo) {
      return Math.abs(a - ((Quaternion) qo).getRpart()) < DELTA &&
              Math.abs(b - ((Quaternion) qo).getIpart()) < DELTA &&
              Math.abs(c - ((Quaternion) qo).getJpart()) < DELTA &&
              Math.abs(d - ((Quaternion) qo).getKpart()) < DELTA;
   }

   /** Dot product of quaternions. (p*conjugate(q) + q*conjugate(p))/2
    * @param q factor
    * @return dot product of this and q
    */
   public Quaternion dotMult (Quaternion q) {
      double dot = a * q.getRpart() + b * q.getIpart() + c * q.getJpart() + d * q.getKpart();
      return new Quaternion(dot, 0., 0., 0.);
   }

   /** Integer hashCode has to be the same for equal objects.
    * @return hashcode
    */
   @Override
   public int hashCode() {
      return Objects.hash(a, b, c ,d);
   }

   /** Norm of the quaternion. Expressed by the formula
    *     norm(a+bi+cj+dk) = Math.sqrt(a*a+b*b+c*c+d*d)
    * @return norm of <code>this</code> (norm is a real number)
    */
   public double norm() {
      return Math.sqrt(a*a + b*b + c*c + d*d);
   }

   /** Main method for testing purposes.
    * @param arg command line parameters
    */
   public static void main (String[] arg) {
      Quaternion arv1 = new Quaternion (-1., 1, 2., -2.);
      if (arg.length > 0)
         arv1 = valueOf (arg[0]);
      System.out.println ("first: " + arv1.toString());
      System.out.println ("real: " + arv1.getRpart());
      System.out.println ("imagi: " + arv1.getIpart());
      System.out.println ("imagj: " + arv1.getJpart());
      System.out.println ("imagk: " + arv1.getKpart());
      System.out.println(arv1.equals(new Quaternion (-1., 1, 2., -2.)));
      System.out.println ("isZero: " + arv1.isZero());
      System.out.println ("conjugate: " + arv1.conjugate());
  /*    System.out.println ("opposite: " + arv1.opposite());
      System.out.println ("hashCode: " + arv1.hashCode());
      Quaternion res = null;
      try {
         res = (Quaternion)arv1.clone();
      } catch (CloneNotSupportedException e) {};
      System.out.println ("clone equals to original: " + res.equals (arv1));
      System.out.println ("clone is not the same object: " + (res!=arv1));
      System.out.println ("hashCode: " + res.hashCode());
      res = valueOf (arv1.toString());
      System.out.println ("string conversion equals to original: "
         + res.equals (arv1));
      Quaternion arv2 = new Quaternion (1., -2.,  -1., 2.);
      if (arg.length > 1)
         arv2 = valueOf (arg[1]);
      System.out.println ("second: " + arv2.toString());
      System.out.println ("hashCode: " + arv2.hashCode());
      System.out.println ("equals: " + arv1.equals (arv2));
      res = arv1.plus (arv2);
      System.out.println ("plus: " + res);
      System.out.println ("times: " + arv1.times (arv2));
      System.out.println ("minus: " + arv1.minus (arv2));
      double mm = arv1.norm();
      System.out.println ("norm: " + mm);
      System.out.println ("inverse: " + arv1.inverse());
      System.out.println ("divideByRight: " + arv1.divideByRight (arv2));
      System.out.println ("divideByLeft: " + arv1.divideByLeft (arv2));
      System.out.println ("dotMult: " + arv1.dotMult (arv2)); */
   }
}
// end of file
