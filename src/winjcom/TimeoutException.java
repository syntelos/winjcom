package winjcom;

import java.io.IOException;

public class TimeoutException extends IOException
  {
  public TimeoutException(String msg)
    {
    super(msg);
    }
  }
