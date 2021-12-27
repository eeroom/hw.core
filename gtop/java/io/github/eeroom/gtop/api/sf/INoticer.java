package io.github.eeroom.gtop.api.sf;

import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.NoticeResponse;

public interface INoticer {

    public NoticeResponse send(NoticeMessage msg);
}
