package com.yixin.garage.dto.message.mail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yixin.garage.dto.message.YxMessageEnvelope;

@JsonIgnoreProperties(ignoreUnknown=true)
public class YxMailMessageDTO extends YxMessageEnvelope
{
  private static final long serialVersionUID = 1L;
  private String mailSubject;
  private String mailText;
  private List<String> mailFileName;
  private List<MailFile> mailFiles;
  private String htmlFlag;
  private String mailTo;
  private String mailBcc;
  private String mailFrom;
  private String mailCc;
  private String priority;
  
  public static long getSerialVersionUID()
  {
    return 1L;
  }
  
  public String getMailSubject()
  {
    return this.mailSubject;
  }
  
  public void setMailSubject(String mailSubject)
  {
    this.mailSubject = mailSubject;
  }
  
  public String getMailText()
  {
    return this.mailText;
  }
  
  public void setMailText(String mailText)
  {
    this.mailText = mailText;
  }
  
  public List<String> getMailFileName()
  {
    return this.mailFileName;
  }
  
  public void setMailFileName(List<String> mailFileName)
  {
    this.mailFileName = mailFileName;
  }
  
  public String getMailTo()
  {
    return this.mailTo;
  }
  
  public void setMailTo(String mailTo)
  {
    this.mailTo = mailTo;
  }
  
  public String getMailBcc()
  {
    return this.mailBcc;
  }
  
  public void setMailBcc(String mailBcc)
  {
    this.mailBcc = mailBcc;
  }
  
  public String getMailFrom()
  {
    return this.mailFrom;
  }
  
  public void setMailFrom(String mailFrom)
  {
    this.mailFrom = mailFrom;
  }
  
  public String getMailCc()
  {
    return this.mailCc;
  }
  
  public void setMailCc(String mailCc)
  {
    this.mailCc = mailCc;
  }
  
  public String getPriority()
  {
    return this.priority;
  }
  
  public void setPriority(String priority)
  {
    this.priority = priority;
  }
  
  public List<MailFile> getMailFiles()
  {
    return this.mailFiles;
  }
  
  public void setMailFiles(List<MailFile> mailFiles)
  {
    this.mailFiles = mailFiles;
  }
  
  public String getHtmlFlag()
  {
    return this.htmlFlag;
  }
  
  public void setHtmlFlag(String htmlFlag)
  {
    this.htmlFlag = htmlFlag;
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("YxMailMessage [");
    if (this.mailSubject != null) {
      builder.append("mailSubject=").append(this.mailSubject).append(", ");
    }
    if (this.mailText != null) {
      builder.append("mailText=").append(this.mailText).append(", ");
    }
    if (this.mailFileName != null) {
      builder.append("mailFileName=").append(this.mailFileName).append(", ");
    }
    if (this.mailFiles != null) {
      builder.append("mailFiles=").append(this.mailFiles).append(", ");
    }
    if (this.htmlFlag != null) {
      builder.append("htmlFlag=").append(this.htmlFlag).append(", ");
    }
    if (this.mailTo != null) {
      builder.append("mailTo=").append(this.mailTo).append(", ");
    }
    if (this.mailBcc != null) {
      builder.append("mailBcc=").append(this.mailBcc).append(", ");
    }
    if (this.mailFrom != null) {
      builder.append("mailFrom=").append(this.mailFrom).append(", ");
    }
    if (this.mailCc != null) {
      builder.append("mailCc=").append(this.mailCc).append(", ");
    }
    if (this.priority != null) {
      builder.append("priority=").append(this.priority).append(", ");
    }
    if (getSenderIpv4Address() != null) {
      builder.append("getSenderIpv4Address()=").append(getSenderIpv4Address()).append(", ");
    }
    if (getSenderDomainAccount() != null) {
      builder.append("getSenderDomainAccount()=").append(getSenderDomainAccount());
    }
    builder.append("]");
    return builder.toString();
  }
}

