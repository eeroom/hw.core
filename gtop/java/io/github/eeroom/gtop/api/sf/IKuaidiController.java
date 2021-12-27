package io.github.eeroom.gtop.api.sf;

import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;
import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeResponse;

public interface IKuaidiController {
    public bizdata newTransfer(EntityByCreate entity);

    public void completePaymoney(EntityByPaymoney entity);

    public NoticeResponse notice(NoticeMessage msg);
}
