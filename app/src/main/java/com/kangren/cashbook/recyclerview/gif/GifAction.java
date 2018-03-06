package com.kangren.cashbook.recyclerview.gif;

/**
 * gif解码回调接口
 * 
 * @author tiantangbao
 * @version [版本号, 2016-1-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface GifAction
{

    /**
     * gif解码过程回调
     * 
     * @param parseStatus 解码是否成功，成功会为true
     * @param frameIndex 当前解码的第几帧，当全部解码成功后，这里为-1
     */
    public void parseOk(boolean parseStatus, int frameIndex);
}
