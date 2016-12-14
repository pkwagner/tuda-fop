//public class Chaos{private static int[] f(int _, int $){if($ == 0)return new
//int[]{_,1,0};int[]$$=f($,_%$);return new int[]{$$[0],$$[2],$$[1]-(_/$)*$$[2]
//};} private static char p(int _,int $,int _$){return (char)(Math.power(_,$)%
//_$);}public static void main(String[]l){int _=3, $=11,_$=_*$,$$=(_-1)*($-1);
//_=7;$=f(_,$$)[1];char[] $_=new char[]{14,20,27,24,8,20,23,23},$$_=$_.clone()
//;$$=-1;while((++$$)<$_.size())$$_[$$]=(char)(p($_[$$],$,_$)+'A');//print it!
//System.out.println($$_);}}//optimized code for Schematic Inc written in 2016