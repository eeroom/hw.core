package io.github.eeroom.hzkd.guonei;



import io.github.eeroom.hzkd.db.bizdata;
import io.github.eeroom.hzkd.viewmodel.ApidataWrapper;
import io.github.eeroom.remoting.proxy.ApiMapping;

public interface IGuoneiKuaidiController {
    @ApiMapping(wrapperType = ApidataWrapper.class)
    public bizdata create(EntityByCreate entity);

    @ApiMapping(wrapperType = ApidataWrapper.class)
    public void paymoney(EntityByPaymoney entity);

}
