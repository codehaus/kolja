package com.baulsupp.kolja.jez;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.baulsupp.kolja.ansi.TailRenderer;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.type.Priority;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLogFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableOutputFormat;
import com.baulsupp.kolja.log.viewer.renderer.Renderer;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    assertNotNull(getFormat());
  }

  public void testParsesStartLine() {
    LineParser lineParser = getLineParser();

    Line line = lineParser
        .parse("starting RunReportAction <*> StartTime[1199863807710] CurrentUser[null] Action[Start] ActionName[RunReport] ActionDescription[, user.name=sysadmin, entryKey=2142, user.password=adric, go--true=foo] FreeMemory[105582120] TotalMemory[268892672] <*> LogThread[ExecuteThread: '22' for queue: 'weblogic.kernel.Default'] LogLevel[INFO ] LogClass[com.ftid.dcs.sickle.nyssa.common.LoggingInterceptor] LogDate[2008-01-09 07:30:07,711]");

    assertFalse(line.isFailed());
    assertEquals("Start", line.getValue(JezConstants.STATE));
    assertEquals("RunReport", line.getValue(JezConstants.ACTION));
    assertEquals("null", line.getValue(JezConstants.USER));
    assertEquals(", user.name=sysadmin, entryKey=2142, user.password=adric, go--true=foo", line.getValue(JezConstants.DESCRIPTION));
    assertEquals(new DateTime(1199863807711l), line.getValue(JezConstants.DATE));
    assertEquals(105582120l, line.getValue(JezConstants.FREE_MEMORY));
    assertEquals(268892672l, line.getValue(JezConstants.TOTAL_MEMORY));

    showLine(line);
  }

  public void testParsesEndLine() {
    LineParser lineParser = getLineParser();

    Line line = lineParser
        .parse("ending EditFundInfoAction <*> StartTime[1199871641935] Duration[68] CurrentUser[gkaminski] Action[End] ActionName[EditFundInfo] ActionDescription[, EditFundInfo.action.y=12, fundCode=HAAGRI, EditFundInfo.action.x=27] FreeMemory[195359168] TotalMemory[268958208] <*> LogThread[ExecuteThread: '11' for queue: 'weblogic.kernel.Default'] LogLevel[INFO ] LogClass[com.ftid.dcs.sickle.nyssa.common.LoggingInterceptor] LogDate[2008-01-09 09:40:42,003]");

    assertFalse(line.isFailed());
    assertEquals("End", line.getValue(JezConstants.STATE));
    assertEquals(68l, line.getValue(JezConstants.DURATION));
    assertEquals("EditFundInfo", line.getValue(JezConstants.ACTION));
    assertEquals("gkaminski", line.getValue(JezConstants.USER));
    assertEquals(", EditFundInfo.action.y=12, fundCode=HAAGRI, EditFundInfo.action.x=27", line.getValue(JezConstants.DESCRIPTION));
    assertEquals(new DateTime(1199871642003l), line.getValue(JezConstants.DATE));
    assertEquals(195359168l, line.getValue(JezConstants.FREE_MEMORY));
    assertEquals(268958208l, line.getValue(JezConstants.TOTAL_MEMORY));

    showLine(line);
  }

  private void showLine(Line line) {
    TailRenderer r = new TailRenderer(getOutputFormat(), true);
    r.show(line);
  }

  public void testParsesSqlLine() {
    LineParser lineParser = getLineParser();

    Line line = lineParser
        .parse("select pk_permission_id,name,description,resource_id,security_level from f137_permission a, f139_role_permission b, f138_user_role c where a.pk_permission_id = b.fk_permission_id and b.fk_role_id = c.fk_role_id and c.fk_user_id = 1 and a.effective_until_dt is null and b.effective_until_dt is null and c.effective_until_dt is null and security_level = 2 union select pk_permission_id,name,description,resource_id,security_level from f137_permission where security_level < 2 and effective_until_dt is null order by 2 <*> LogThread[ExecuteThread: '22' for queue: 'weblogic.kernel.Default'] LogLevel[INFO ] LogClass[com.ftid.dcs.sickle.nyssa.permissions.UserDelegateJDBC] LogDate[2008-01-09 08:00:03,300]");

    assertFalse(line.isFailed());
    assertEquals(
        "select pk_permission_id,name,description,resource_id,security_level from f137_permission a, f139_role_permission b, f138_user_role c where a.pk_permission_id = b.fk_permission_id and b.fk_role_id = c.fk_role_id and c.fk_user_id = 1 and a.effective_until_dt is null and b.effective_until_dt is null and c.effective_until_dt is null and security_level = 2 union select pk_permission_id,name,description,resource_id,security_level from f137_permission where security_level < 2 and effective_until_dt is null order by 2",
        line.getValue(JezConstants.SQL));
    assertEquals("ExecuteThread: '22' for queue: 'weblogic.kernel.Default'", line.getValue(JezConstants.LOG_THREAD));
    assertEquals(Priority.INFO, line.getValue(JezConstants.LOG_LEVEL));
    assertEquals("com.ftid.dcs.sickle.nyssa.permissions.UserDelegateJDBC", line.getValue(JezConstants.LOG_CLASS));
    assertEquals(new DateTime(1199865603300l), line.getValue(JezConstants.DATE));

    showLine(line);
  }

  private LineParser getLineParser() {
    LineParser lineParser = getFormat().getLineParser();

    assertNotNull(lineParser);

    return lineParser;
  }

  private Renderer<Line> getOutputFormat() {
    ConfigurableOutputFormat outputFormat = getFormat().getOutputFormat();

    assertNotNull(outputFormat);

    return outputFormat.getRenderer();
  }

  private ConfigurableLogFormat getFormat() {
    ApplicationContext ac = new FileSystemXmlApplicationContext("src/main/config/jez.xml");

    return (ConfigurableLogFormat) ac.getBean("logFormat");
  }
}
