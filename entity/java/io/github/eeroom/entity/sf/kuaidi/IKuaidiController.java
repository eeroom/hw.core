package io.github.eeroom.entity.sf.kuaidi;

import io.github.eeroom.entity.sf.db.bizdata;

public interface IKuaidiController {
    public bizdata newTransfer(io.github.eeroom.entity.sf.kuaidi.EntityByCreate entity);

    public void completePaymoney(EntityByPaymoney entity);

    public NoticeResponse notice(NoticeMessage msg);
}
