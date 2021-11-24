import java.util.*;
import java .io .*;
 class Pair{
  public int First;
  public int  Second;
  }
 class TreeNode{
	public TreeNode parent;
	public TreeNode left;
	public TreeNode right;
	public Pair xrange;
	public Pair yrange;
	public Pair val;
	public int depth;
	public int numberleaves;
	public boolean isLeaf;
}
public class kdtree
{
	public TreeNode rootnode=new TreeNode();
  public void sort(List<Pair> v, int x, int y, int depth){
    for(int i = x; i < y; i++){
      int min = Integer.MAX_VALUE;
      int idx = -1;
      for(int j = i; j <= y; j++){
        if(depth%2==0){
          if(min > v.get(j).First){
            min = v.get(j).First;
            idx = j;
          }
        }
        else{
          if(min > v.get(j).Second){
            min = v.get(j).Second;
            idx = j;
          }
        }
      }
      if(depth%2==0){
        int temp = v.get(idx).Second;
        v.get(idx).First = v.get(i).First;
        v.get(idx).Second = v.get(i).Second;
        v.get(i).First = min;
        v.get(i).Second = temp;
      }   
      else{
        int temp = v.get(idx).First;
        v.get(idx).Second = v.get(i).Second;
        v.get(idx).First = v.get(i).First;
        v.get(i).First = temp;
        v.get(i).Second = min;
      }
    }
  }
	public void treebuild(List<Pair> v,TreeNode N1,TreeNode parent,int child)//child=1 for left,child=2 for right
	{
		TreeNode N=new TreeNode();
		if(parent!=null)
			N.depth=parent.depth+1;
		else
			N.depth=0;
	  N.numberleaves=v.size();
    if(child==1)
      parent.left=N;
    else if(child==2)
      parent.right=N;
		N.parent=parent;
        if(v.size()==1)
        	{
             Pair t=new Pair();
             t.First=v.get(0).First;
             t.Second=v.get(0).Second;
        	   N.val=t;
             Pair p1=new Pair();
             Pair p2=new Pair();
             p1.First=v.get(0).First;
             p1.Second=v.get(0).First;
             p2.First=v.get(0).Second;
             p2.Second=v.get(0).Second;
             N.xrange=p1;
             N.yrange=p2;
        	   N.isLeaf=true;
        	   N1=N;
             if(N1.depth==0)
              rootnode=N1;
             //System.out.println(N1.val.First);
        	}
        else{
          int n=v.size(),c=1;
          int x1=v.get(0).First,x2=x1,y1=v.get(0).Second,y2=y1;
          for(int i=1;i<v.size();++i)
          {
            if(v.get(i).First>x2)
            	x2=v.get(i).First;
            else if(v.get(i).First<x1)
            	x1=v.get(i).First;
            if(v.get(i).Second>y2)
            	y2=v.get(i).Second;
            else if(v.get(i).Second<y1)
            	y1=v.get(i).Second;
          }
          Pair p1=new Pair();
          Pair p2=new Pair();
          p1.First=x1;
          p1.Second=x2;
          p2.First=y1;
          p2.Second=y2;
          N.xrange=p1;
          N.yrange=p2;
          List<Pair> v1=new ArrayList<Pair>();
          List<Pair> v2=new ArrayList<Pair>();
          sort(v,N.depth,0,v.size()-1);
          if(n%2==0)
          	 {
          	 	while(v1.size()<n/2)
          	 	  v1.add(v.get(v1.size()));
          	 	while(v2.size()<n/2)
          	 	  v2.add(v.get(v1.size()+v2.size()));
          	 	N.val=v.get(n/2-1);
          	 }
           else
           {
           	N.val=v.get(n/2);
          	while(v1.size()<=n/2)
          	 v1.add(v.get(v1.size()));
          	while(v2.size()<n/2)
          	 v2.add(v.get(v1.size()+v2.size()));
           }
            N1=N;
            treebuild(v1,N1.left,N1,1);
          	treebuild(v2,N1.right,N1,2);	
            if(N1.depth==0)
              rootnode=N1;
           // System.out.println(N1.val.First);
        }
	}
	public int findres(Pair r,TreeNode N)
	{
	  int x1,x2,y1,y2;
	  x2=r.First+100;
	  x1=r.First-100;
	  y2=r.Second+100;
	  y1=r.Second-100;
    if(N==null)
      return 0;
    else{
	  boolean c1=(N.xrange.First>=x1);
	  boolean c2=(N.xrange.Second<=x2);
	  boolean c3=(N.yrange.First>=y1);
	  boolean c4=(N.yrange.Second<=y2);
      if(c1&&c2&&c3&&c4)
         return N.numberleaves;
      else
      {
        if((N.xrange.Second<x1)||(N.xrange.First>x2))
          return 0;
        else if((N.yrange.Second<y1)||(N.yrange.First>y2))
          return 0;
      	else
          return findres(r,N.left)+findres(r,N.right);
      }
    }    
	}
	public static void main ( String args [])
 {
 	kdtree k=new kdtree();
 	List<Pair> v=new ArrayList<Pair>();
    try {
      FileInputStream fstream =new FileInputStream ("restaurants.txt");
      Scanner s = new Scanner ( fstream );
      int flag=0;
      while(s.hasNextLine())
         {
         	String s1=s.nextLine();
         	int x,y;
         	int c=0;
         	while(s1.charAt(c)!=',')
         	   c++;
         	try{
         	x=Integer.parseInt(s1.substring(0,c));
         	y=Integer.parseInt(s1.substring(c+1,s1.length()));
          Pair p=new Pair();
          p.First=x;
          p.Second=y;
          v.add(p);
         }
         	catch(NumberFormatException e){
         	}
         }
    } catch ( FileNotFoundException e) {
      System . out . println (" File not found ");
       } 
   k.treebuild(v,k.rootnode,null,0); 
   List<Pair> v3=new ArrayList<Pair>();
    try {
      FileInputStream fstream =new FileInputStream ("queries.txt");
      Scanner s = new Scanner ( fstream );
      int flag=0;
      while (s. hasNextLine ())
         {
         	String s1=s.nextLine();
         	int x,y;
         	int c=0;
         	while(s1.charAt(c)!=',')
         	   c++;
         	try{
         	x=Integer.parseInt(s1.substring(0,c));
         	y=Integer.parseInt(s1.substring(c+1,s1.length()));
          Pair p=new Pair();
          p.First=x;
          p.Second=y;
          v3.add(p);
         }
         catch(NumberFormatException e){
         }
         }
    } catch ( FileNotFoundException e) {
      System . out . println (" File not found ");
       }
      try {
       FileOutputStream fs = new FileOutputStream ("output.txt",false);
       PrintStream p = new PrintStream (fs );
       int c=0;
       while(c<v3.size())
       {
       	 p.print(k.findres(v3.get(c),k.rootnode));
       	 p.print("\n");
       	 c++;
       }
     } catch ( FileNotFoundException e1) {
      System . out . println (" File not found ");
         }
}

  /*public static void PrintTree(TreeNode rootnode){
      Queue<TreeNode> q= new LinkedList<>();
      q.add(rootnode);

      while(!q.isEmpty()){
        TreeNode tnode = q.remove();

        System.out.print("(" + tnode.xrange.First + ", " + tnode.xrange.Second + ") ");
         System.out.print("\n"+"(" + tnode.yrange.First + ", " + tnode.yrange.Second + ") ");
        if(tnode.left != null)
          q.add(tnode.left);
        if(tnode.right != null){
          q.add(tnode.right);
        }
      }
  }*/
}