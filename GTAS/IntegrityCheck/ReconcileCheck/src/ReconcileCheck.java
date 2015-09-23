import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ReconcileCheck
{
  private String _gtReport;
  private String _xbReport;
  private BufferedReader _gtReportFile;
  private BufferedReader _xbReportFile;
  private String _xbHeader, _gtHeader;
  private Hashtable _xbTable = new Hashtable();
  private Hashtable _gtTable = new Hashtable();
  private ArrayList _xbDupList = new ArrayList();
  private File _reportFolder;
  
  public static void main(String[] args)
  {
    if (args.length < 2)
    {
      printUsage();
      System.exit(-1);  
    }
    
    ReconcileCheck reconCheck = new ReconcileCheck(args[0], args[1]);
    
    try
    {
      reconCheck.start(); 
      System.exit(0);
    }
    catch (Exception e)
    {
      System.err.println("Reconcile Check Error: "+e.getMessage());
      e.printStackTrace();
      System.exit(-3);
    }
  }
  
  static void printUsage()
  {
    StringBuffer buff = new StringBuffer();
    buff.append("ReconcileCheck expects Parameters as follows:-\n")
      .append("Param 1: GridTalk complete_wexport_list report Filename\n")
      .append("Param 2: XB docheader report Filename\n");
    System.out.println(buff.toString());  
  }

  BufferedReader createReader(String filename) throws Exception
  {
    return new BufferedReader(new FileReader(filename));  
  }

  Writer createWriter(String filename) throws Exception
  {
    BufferedWriter w = new BufferedWriter(new FileWriter(new File(_reportFolder,filename)));
    return w;
  }
  
  public ReconcileCheck(String gtReport, String xbReport)
  {
    _gtReport = gtReport;
    _xbReport = xbReport;
  }
  
  public void start() throws Exception
  {
    _reportFolder = new File("reconciliation");
    _reportFolder.mkdir();

    readXbTable();
    readGtTable();
    writeXbNoMatchFile();
    writeXbDupFile();
    writeGtNoMatchFile();
  }
  
  void writeXbNoMatchFile() throws Exception
  {
    System.out.println("Writing xb_nomatch_list.csv...");
    Writer w = createWriter("xb_nomatch_list.csv");
    w.write(_xbHeader.concat("\n"));
    
    System.out.println("Num Xb nomatch records = "+_xbTable.size());
    for (Iterator i=_xbTable.keySet().iterator(); i.hasNext(); )
    {
      String record = (String)_xbTable.get(i.next());
      w.write(record.concat("\n"));
    }
    w.close();
  }
  
  void writeXbDupFile() throws Exception
  {
    System.out.println("Writing xb_dup_list.csv...");
    Writer w = createWriter("xb_dup_list.csv");
    w.write(_xbHeader.concat("\n"));
    
    System.out.println("Num Xb duplicate records = "+_xbDupList.size());
    for (Iterator i=_xbDupList.iterator(); i.hasNext(); )
    {
      String record = (String)i.next();
      w.write(record.concat("\n"));
    }
    w.close();
  }
  
  void writeGtNoMatchFile() throws Exception
  {
    System.out.println("Writing gt_nomatch_list.csv...");
    Writer w = createWriter("gt_nomatch_list.csv");
    w.write(_gtHeader.concat("\n"));
    
    System.out.println("Num Gt nomatch records = "+_gtTable.size());
    for (Iterator i=_gtTable.keySet().iterator(); i.hasNext(); )
    {
      String record = (String)_gtTable.get(i.next());
      w.write(record.concat("\n"));
    }
    w.close();
  }
  
  void readGtTable() throws Exception
  {
    System.out.println("Reading GtReport...");
    _gtReportFile = createReader(_gtReport);
    _gtHeader = _gtReportFile.readLine();    
    int gtEpGdocIdPos = getPosition(_gtHeader, "EPGdocId");

    String record;
    String epGdocId;
    while ((record = _gtReportFile.readLine()) != null)
    {
      StringTokenizer st = new StringTokenizer(record, ",");
      for (int i=0; i<gtEpGdocIdPos; i++)
        st.nextToken();
      epGdocId = st.nextToken();
      if (_xbTable.remove(epGdocId) == null)
      {
        _gtTable.put(epGdocId, record);
      }
      
    }
  }
  
  void readXbTable() throws Exception
  {
    System.out.println("Reading XbReport...");
    _xbReportFile = createReader(_xbReport);
    _xbHeader = _xbReportFile.readLine();
    int xbEpGdocIdPos = getPosition(_xbHeader, "gdoc_id");
    
    String record;
    String gdocId;
    while ((record = _xbReportFile.readLine()) != null)
    {
      StringTokenizer st = new StringTokenizer(record, ",");
      for (int i=0; i<xbEpGdocIdPos; i++)
        st.nextToken();
      gdocId = st.nextToken();
      if (_xbTable.containsKey(gdocId))
        _xbDupList.add(record); 
      else
        _xbTable.put(gdocId, record);   
    }
    System.out.println("Num Xb distinct records = "+_xbTable.size());
    System.out.println("Num Xb duplicate records = "+_xbDupList.size());
  }
  
  int getPosition(String header, String colName) throws Exception
  {
    if (header != null)
    {
      StringTokenizer st = new StringTokenizer(header, ",");
      int pos = -1;
      while (st.hasMoreTokens())
      {
        pos++;
        if (colName.equals(st.nextToken()))
        {
          return pos;
        }
      }
      return -1;
    }
    else
      return -1;
  }
}
