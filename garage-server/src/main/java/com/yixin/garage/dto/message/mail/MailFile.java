package com.yixin.garage.dto.message.mail;

public class MailFile
{
  private String fileSourceName;
  private String fileUrl;
  
  public String getFileSourceName()
  {
    return this.fileSourceName;
  }
  
  public void setFileSourceName(String fileSourceName)
  {
    this.fileSourceName = fileSourceName;
  }
  
  public String getFileUrl()
  {
    return this.fileUrl;
  }
  
  public void setFileUrl(String fileUrl)
  {
    this.fileUrl = fileUrl;
  }
}

