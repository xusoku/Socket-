/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\xusoku\\Desktop\\mediaProxy\\downloadVideo\\src\\com\\cooge\\media\\proxy\\model\\IMsgService.aidl
 */
package com.cooge.media.proxy.model;
public interface IMsgService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.cooge.media.proxy.model.IMsgService
{
private static final java.lang.String DESCRIPTOR = "com.cooge.media.proxy.model.IMsgService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.cooge.media.proxy.model.IMsgService interface,
 * generating a proxy if needed.
 */
public static com.cooge.media.proxy.model.IMsgService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.cooge.media.proxy.model.IMsgService))) {
return ((com.cooge.media.proxy.model.IMsgService)iin);
}
return new com.cooge.media.proxy.model.IMsgService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getPrice:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
DownMsg _result = this.getPrice(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.cooge.media.proxy.model.IMsgService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public DownMsg getPrice(java.lang.String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
DownMsg _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_getPrice, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = DownMsg.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getPrice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public DownMsg getPrice(java.lang.String key) throws android.os.RemoteException;
}
