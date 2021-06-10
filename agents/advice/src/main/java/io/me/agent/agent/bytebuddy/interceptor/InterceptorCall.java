package io.me.agent.agent.bytebuddy.interceptor;

/**
 * 在拦截器使用方式下，如果需要配合{@link net.bytebuddy.implementation.bind.annotation.Morph}来使用的话，
 * 需要一个接口，提供唯一一个方法，且除方法名外，方法的返回值必须为Object类型，方法参数必须是一个Object数组类型（变长参数）
 */
public interface InterceptorCall {

    Object invoke(Object... args);

}
