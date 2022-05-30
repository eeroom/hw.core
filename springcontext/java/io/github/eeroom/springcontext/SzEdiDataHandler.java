package io.github.eeroom.springcontext;

public class SzEdiDataHandler implements IEdiDataHandler {
    @Override
    public int processReqest(String data) {
        System.out.println("processReqest,data:"+data);
        return 0;
    }
}
