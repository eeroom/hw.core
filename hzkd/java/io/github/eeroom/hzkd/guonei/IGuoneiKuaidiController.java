package io.github.eeroom.hzkd.guonei;



import io.github.eeroom.hzkd.db.bizdata;
import io.github.eeroom.hzkd.httpclient.ApiMapping;
import io.github.eeroom.hzkd.viewmodel.ApidataWrapper;

public interface IGuoneiKuaidiController {
    @ApiMapping(wrapperType = ApidataWrapper.class)
    public bizdata create(EntityByCreate entity);

    @ApiMapping(wrapperType = ApidataWrapper.class)
    public void paymoney(EntityByPaymoney entity);

}
