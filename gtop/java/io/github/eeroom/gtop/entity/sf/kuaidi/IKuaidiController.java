package io.github.eeroom.gtop.entity.sf.kuaidi;

import io.github.eeroom.gtop.entity.sf.db.bizdata;

public interface IKuaidiController {
    public bizdata newTransfer(EntityByCreate entity);

    public void completePaymoney(EntityByPaymoney entity);

    public NoticeResponse notice(NoticeMessage msg);
}
