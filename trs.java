import java.util.*;
//import static org.junit.Assert.assertEquals;

class Point{
  int x;
  int y;
  String f;
  boolean placed;
  public Point(int x, int y, String f){
    this.x = x;
    this.y = y;
    this.f = f;
    placed = false;
  }

  @Override
  public String toString(){
      return this.x + "," + this.y + "," + this.f;
  }
  
  public void reset(){
      this.x = 0;
      this.y = 0;
      this.f = "";
      this.placed = false;
  }
}

public class TRS{
  
  static final String PLACE = "PLACE";
  static final String MOVE = "MOVE";
  static final String LEFT = "LEFT";
  static final String RIGHT = "RIGHT";
  static final String REPORT = "REPORT";
  
  static final String NORTH = "NORTH";
  static final String WEST = "WEST";
  static final String SOUTH = "SOUTH";
  static final String EAST = "EAST";
  // directions array
  static List<String> dirs;
  static int n, m;
  Map<String, List<Integer>> moveMap;
  Point p;
  public TRS(){
      n = 5;
      m = 5;
      p = new Point(0 , 0, "");
      dirs = new ArrayList<String>();
      dirs.add(NORTH);
      dirs.add(WEST);
      dirs.add(SOUTH);
      dirs.add(EAST);
      moveMap = new HashMap<String, List<Integer>>();
      // delta of x, y for next move
      List<Integer> dlt = new ArrayList<Integer>();
      // init moveMap
      // a MOVE at (x, y) when facing north would result in (x ,y+1)
      dlt.add(0);
      dlt.add(1);
      moveMap.put(NORTH, dlt);
      dlt = new ArrayList<Integer>();
      dlt.add(-1);
      dlt.add(0);
      moveMap.put(WEST, dlt);
      dlt = new ArrayList<Integer>();
      dlt.add(0);
      dlt.add(-1);
      moveMap.put(SOUTH, dlt);
      dlt = new ArrayList<Integer>();
      dlt.add(1);
      dlt.add(0);
      moveMap.put(EAST, dlt);
  }

  public void process(List<String> cmds){
      
      for(String cmd: cmds){
          // read in cmds until first valid PLACE cmd
          if(!p.placed){
              if(!cmd.startsWith(PLACE) || !action(cmd)){
                  continue;
              }
          }else{
              // if already placed, action on the cmd
              action(cmd);
          }
      }
  }

  public boolean action(String cmd){
      // verify if it's a valid cmd, and action on valid cmd
      if(cmd.startsWith(PLACE)){
          String[] parts = cmd.split(" ");
          String[] subs = parts[1].split(",");
          int x = Integer.parseInt(subs[0]);
          int y = Integer.parseInt(subs[1]);
          if(x >= 0 && x < m && y >= 0 && y < n && moveMap.containsKey(subs[2])){
              p.x = x;
              p.y = y;
              p.f = subs[2];
              p.placed = true;
              return true;
          }else{
              return false;
          }
          
      }else if(MOVE.equals(cmd)){
          // get delta array, make move
          List<Integer> da = moveMap.get(p.f);
          int nx = p.x + da.get(0);
          int ny = p.y + da.get(1);
          if(nx < 0 || nx >= m || ny < 0 || ny >= n){
              return false;
          }else{
              p.x = nx;
              p.y = ny;
              return true;
          }
          
      }else if(LEFT.equals(cmd)){
          // update facing direction
          int idx = dirs.indexOf(p.f);
          p.f = dirs.get((idx+1)%4);
          return true;
      }else if(RIGHT.equals(cmd)){
          int idx = dirs.indexOf(p.f);
          p.f = dirs.get((idx+4-1)%4);
          return true;
      }else if(REPORT.equals(cmd)){
          System.out.println(p.toString());
          return true;
      }
      return false;
  }
  
  // (TODO) test functions would be in a separate file
  public void testTRS(){
    ArrayList[] list = new ArrayList[6];
    String[] expected = new String[6];

    int k = 0;
    list[k] = new ArrayList<String>();
    list[k].add("PLACE 0,0,NORTH");
    list[k].add("MOVE");
    list[k].add("REPORT");
    expected[k] = "0,1,NORTH";
    k = 1;
    list[k] = new ArrayList<String>();
    list[k].add("PLACE 0,0,NORTH");
    list[k].add("LEFT");
    list[k].add("REPORT");
    expected[k] = "0,0,WEST";
    k = 2;
    list[k] = new ArrayList<String>();
    list[k].add("PLACE 1,2,EAST");
    list[k].add("MOVE");
    list[k].add("MOVE");
    list[k].add("LEFT");
    list[k].add("MOVE");
    list[k].add("REPORT");
    expected[k] = "3,3,NORTH";
    k = 3;
    list[k] = new ArrayList<String>();
    list[k].add("MOVE");
    list[k].add("RIGHT");
    list[k].add("PLACE 9,2,EAST");
    list[k].add("PLACE 2,0,EAST");
    list[k].add("MOVE");
    list[k].add("LEFT");
    list[k].add("MOVE");
    list[k].add("REPORT");
    expected[k] = "3,1,NORTH";
    k = 4;
    list[k] = new ArrayList<String>();
    list[k].add("PLACE 3,4,NORTH");
    list[k].add("MOVE");
    list[k].add("RIGHT");
    list[k].add("MOVE");
    list[k].add("MOVE");
    list[k].add("REPORT");
    expected[k] = "4,4,EAST";
    k = 5;
    list[k] = new ArrayList<String>();
    list[k].add("PLACE 2,3,NORTH");
    list[k].add("MOVE");
    list[k].add("PLACE 1,2,WEST");
    list[k].add("RIGHT");
    list[k].add("MOVE");
    list[k].add("RIGHT");
    list[k].add("MOVE");
    list[k].add("REPORT");
    expected[k] = "2,3,EAST";
    for(int i = 0; i <= k; i++){
      if(testMOVES(list[i], expected[i])){
        System.out.println("test "+ i + " passed"); 
      }else{
        System.out.println("test "+ i + " failed"); 
      }
      p.reset();
    }
  }
  
  public boolean testMOVES(List<String> cmds, String expected){
      process(cmds);
      return expected.equals(p.toString());
      //assertEquals(p.toString(), expected);
  }
  
  public static void main(String []args){
    TRS trs = new TRS();
    trs.testTRS();
  }
}
