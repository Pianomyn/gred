package org.pianomyn.gred.matching;

public enum AlgorithmType {
  NAIVE("nv"),
  RABIN_KARP("rk"),
  BOYER_MOORE("bm"),
  KMP("kmp");

  public final String flag;

  AlgorithmType(String flag) {
    this.flag = flag;
  }
}
