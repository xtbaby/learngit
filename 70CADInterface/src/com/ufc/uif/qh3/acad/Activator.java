package com.ufc.uif.qh3.acad;

import java.net.Socket;

import org.dom4j.Document;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ufc.uif.adaptor.server.service.IComponentProcess;
import com.ufc.uif.adaptor.server.service.InitializationException;
import com.ufc.uif.qh3.acad.util.BundleContextUtil;

public class Activator implements BundleActivator {

  public void start(BundleContext context) throws Exception {
    BundleContextUtil.setContext(context);
    context.registerService(IComponentProcess.class.getName(), new IComponentProcess() {
      Qh3_CADAdaptor adaptor;
      Document message;
      public Thread getProcessThread(Socket socket) throws InitializationException {
        adaptor = new Qh3_CADAdaptor(socket);
        adaptor.setMessage(message);
        return new Thread(adaptor);
      }
      public String getComponentName() {
        return "ACAD";
      }
      public void setMessage(Document doc) {
        this.message = doc;
      }
      @SuppressWarnings("unused")
      public Thread getProcessThread(Socket arg0, Document arg1) throws InitializationException {
        return null;
      }
    }, null);
  }

  public void stop(BundleContext context) throws Exception {
  }
}
