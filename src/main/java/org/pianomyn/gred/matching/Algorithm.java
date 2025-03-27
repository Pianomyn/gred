package org.pianomyn.gred.matching;

public enum Algorithm {
  NAIVE("nv"),
  RABIN_KARP("rk"),
  BOYER_MOORE("bm"),
  KMP("kmp");

  public final String flag;

  Algorithm(String flag) {
    this.flag = flag;
  }
}
