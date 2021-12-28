package io.github.eeroom.gtop.api.sf.kuaidi;

import io.github.eeroom.gtop.entity.sf.db.bizdata;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByCreate;
import io.github.eeroom.gtop.entity.sf.kuaidi.EntityByPaymoney;

public interface IGuoneiKuaidiController {
    public bizdata create(EntityByCreate entity);

    public void paymoney(EntityByPaymoney entity);

}
