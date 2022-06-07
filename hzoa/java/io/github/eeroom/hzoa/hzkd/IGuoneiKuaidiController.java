package io.github.eeroom.hzoa.hzkd;

import io.github.eeroom.remoting.proxy.ApiMapping;

public interface IGuoneiKuaidiController {
    @ApiMapping(wrapperType = ApidataWrapper.class)
    public bizdata create(EntityByCreate entity);

    @ApiMapping(wrapperType = ApidataWrapper.class)
    public void paymoney(EntityByPaymoney entity);

}
