package com.ufc.uif.qh3.acad.util;

import org.osgi.framework.BundleContext;

public class BundleContextUtil {
private static   BundleContext context;

public static BundleContext getContext() {
  return context;
}

public static void setContext(BundleContext context) {
  BundleContextUtil.context = context;
}




 
}
