package com.yixin.garage.dto.message;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.shiro.SecurityUtils;

public class YxMessageEnvelope
implements Serializable
{
private static final long serialVersionUID = 8303701103695233071L;
private static final String UNKNOWN = "unknown";
private static final String SENDER_IPV4_ADDRESS;
private String senderDomainAccount;
private String senderIpv4Address;
private String senderChannel;
private String msgId;

static
{
  String ipv4Address = null;
  StringBuilder sb = new StringBuilder();
  try
  {
    Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
    while (ifaces.hasMoreElements())
    {
      NetworkInterface iface = (NetworkInterface)ifaces.nextElement();
      
      Enumeration<InetAddress> addresses = iface.getInetAddresses();
      while (addresses.hasMoreElements())
      {
        InetAddress addr = (InetAddress)addresses.nextElement();
        if (((addr instanceof Inet4Address)) && 
          (!addr.isLoopbackAddress())) {
          sb.append(addr.getHostAddress()).append(",");
        }
      }
    }
    ipv4Address = sb.toString();
  }
  catch (Throwable e)
  {
    ipv4Address = null;
  }
  SENDER_IPV4_ADDRESS = (ipv4Address != null) && (ipv4Address.length() > 0) ? ipv4Address.substring(0, ipv4Address.length() - 1) : "unknown";
}

public void setSenderDomainAccount(String domainAccount)
{
  String domainAccountOfShiro = null;
  if ((domainAccount == null) || (domainAccount.length() == 0)) {
    try
    {
      domainAccountOfShiro = (String)SecurityUtils.getSubject().getSession().getAttribute("username_shiro");
    }
    catch (Throwable e)
    {
      domainAccount = "unknown";
    }
  }
  this.senderDomainAccount = (domainAccount != null ? domainAccount : domainAccountOfShiro);
}

public String getSenderChannel()
{
  return this.senderChannel;
}

public void setSenderChannel(String senderChannel)
{
  this.senderChannel = senderChannel;
}

public YxMessageEnvelope()
{
  this.senderIpv4Address = SENDER_IPV4_ADDRESS;
  this.senderDomainAccount = "unknown";
}

public String getSenderIpv4Address()
{
  return this.senderIpv4Address;
}

public String getSenderDomainAccount()
{
  return this.senderDomainAccount;
}

public void setSenderIpv4Address(String senderIpv4Address)
{
  this.senderIpv4Address = senderIpv4Address;
}

public String getMsgId()
{
  return this.msgId;
}

public void setMsgId(String msgId)
{
  this.msgId = msgId;
}

public String toString()
{
  StringBuilder builder = new StringBuilder();
  builder.append("YxMessageEnvelope [");
  if (this.senderDomainAccount != null)
  {
    builder.append("senderDomainAccount=");
    builder.append(this.senderDomainAccount);
    builder.append(", ");
  }
  if (this.senderIpv4Address != null)
  {
    builder.append("senderIpv4Address=");
    builder.append(this.senderIpv4Address);
    builder.append(", ");
  }
  if (this.senderChannel != null)
  {
    builder.append("senderChannel=");
    builder.append(this.senderChannel);
    builder.append(", ");
  }
  if (this.msgId != null)
  {
    builder.append("msgId=");
    builder.append(this.msgId);
  }
  builder.append("]");
  return builder.toString();
}
}

