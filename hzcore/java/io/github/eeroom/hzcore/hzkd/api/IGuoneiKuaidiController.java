package io.github.eeroom.hzcore.hzkd.api;

import io.github.eeroom.apiclient.ApiMapping;
import io.github.eeroom.hzcore.ApidataWrapper;
import io.github.eeroom.hzcore.hzkd.db.bizdata;
import io.github.eeroom.hzcore.hzkd.guonei.EntityByCreate;
import io.github.eeroom.hzcore.hzkd.guonei.EntityByPaymoney;

public interface IGuoneiKuaidiController {
    @ApiMapping(wrapperType = ApidataWrapper.class)
    public bizdata create(EntityByCreate entity);

    @ApiMapping(wrapperType = ApidataWrapper.class)
    public void paymoney(EntityByPaymoney entity);

}
