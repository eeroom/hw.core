package io.github.eeroom.entity.sf.kuaidi;

public interface IKuaidiController {
    public io.github.eeroom.entity.sfdb.bizdata newTransfer(io.github.eeroom.entity.sf.kuaidi.EntityByCreate entity);

    public void completePaymoney(EntityByPaymoney entity);

    public NoticeResponse notice(NoticeMessage msg);
}
