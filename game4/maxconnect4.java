import java.util.*;
import java.io.*;

class gameplayboard
{
	int[][] gameplayboard=new int[6][7];
	int gameturn=0;
	
	public gameplayboard(String inputfile)
	{
		gameplayboard=new int[6][7];
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(inputfile));
			for(int a=0;a<gameplayboard.length;a++)
			{
				String line=br.readLine();
				for(int b=0;b<gameplayboard[a].length;b++)
				{
					gameplayboard[a][b]=Character.getNumericValue(line.charAt(b));
				}
			}
			String line=br.readLine();
			this.gameturn=Character.getNumericValue(line.charAt(0));
		}
		catch(Exception e)
		{
			gameplayboard= new int[][]{
			{0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0}
			};
		}
	}
	//to check the move is valid 
	public boolean isplaymovevalid(int c)
	{
		return gameplayboard[0][c]==0;
	}
	//method plays a piece on the board
	public boolean playpiece(int c, int gamer)
	{
		if(!isplaymovevalid(c))
		{
			System.out.println("illegal move!");
			return false;
		}
		for(int a=5;a>=0;--a)
		{
			if(gameplayboard[a][c] == 0)
			{
				gameplayboard[a][c] = gamer;
				return true;
			}
		}
		return false;
	}
	//method removes the top piece from the board
	public void removeapiece(int c)
	{
		for(int a=0;a<=5;++a)
		{
			if(gameplayboard[a][c] !=0)
			{
				gameplayboard[a][c] =0;
				break;
			}
		}
	}
	//print the game board
	public void outputgameboard()
	{
		System.out.println();
		for(int a=0; a<=5;++a)
		{
			for(int b=0;b<=6;++b)
			{
				System.out.print(gameplayboard[a][b]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
//maxconnect4 game
public class maxconnect4
{
	private Scanner scan;
	private int humanplayer=0;
	private int aiplayer=0;
	private int nextplayermove=-1;
	private int maxdepthlimit=0;
	private gameplayboard g;

	public static void main(String[] args)throws Exception
	{
		if(args.length!=4)
		{
			System.out.println("Four Cmd Line arguments are needed:\n" + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n" + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");
            			System.exit( 0 );
        		}	
		String typeofgame=args[0].toString();
		String fileinput= args[1].toString();
		gameplayboard g=new gameplayboard(fileinput);
		int gameturn=g.gameturn;
		maxconnect4 aiplayer= new maxconnect4(g);
		aiplayer.maxdepthlimit=Integer.parseInt(args[3].toString());
		if(typeofgame.equalsIgnoreCase("interactive"))
		{
			String nextplayermove=args[2].toString();
			if(nextplayermove.equalsIgnoreCase("computer-next"))
			aiplayer.gameplaystart(1);
			else if(nextplayermove.equalsIgnoreCase("human-next"))
			aiplayer.gameplaystart(2);
			else
			{
				System.out.println("improper input");
				System.exit(0);
			}
		}
		else if (typeofgame.equalsIgnoreCase("one-move"))
		{
			String fileoutput=args[2].toString();
			aiplayer.gameplaystart1(g,fileoutput);
		}
		else
		{
			System.out.println("Game mode not present");
			System.exit(0);
		}
	}
	//for the interactive mode game
	public void gameplaystart(int gameturn) throws Exception
	{
		if(gameturn==2)
		{
			g.outputgameboard();
			oppositionmove();
		}
		g.outputgameboard();
		outputfile(g,2);
		System.out.println("AI move:");
		if(initialboardstate(g))
		g.playpiece(3,1);
		else
		g.playpiece(computerturn(),1);
		g.outputgameboard();
		outputfile(g,1);
	
		while(true)
		{
			oppositionmove();
			g.outputgameboard();
			outputfile(g,2);
			int finalgameboardresult=finalgameboardresult(g);
			if(finalgameboardresult==1)
			{
				System.out.println("AI wins!");
				System.out.println("AI score is: "+aiplayer); 										System.out.println("Human score is: "+humanplayer); 		
				break;
			}
            			else if(finalgameboardresult==2)
			{
				System.out.println("You Win!");
				System.out.println("AI score is: "+aiplayer);
 				System.out.println("Human score is: "+humanplayer); 				
				break;
			}
            			else if(finalgameboardresult==0)
			{
				System.out.println("Draw!");
 				System.out.println("AI score is: "+aiplayer); 				
				System.out.println("Human score is: "+humanplayer);
				break;
			}
            			System.out.println("AI move:");
			g.playpiece(computerturn(),1);
			g.outputgameboard();
			outputfile(g,1);
			finalgameboardresult=finalgameboardresult(g);
			if(finalgameboardresult==1)
			{
				System.out.println("AI Wins!"); 
				System.out.println("AI score is: "+aiplayer); 				
				System.out.println("Human score is: "+humanplayer); 				
				break;
			}
            			else if(finalgameboardresult==2)
			{
				System.out.println("You Win!"); 
				System.out.println("AI score is: "+aiplayer); 				
				System.out.println("Human score is: "+humanplayer); 
				break;
			}
            			else if(finalgameboardresult==0)
			{
				System.out.println("Draw!"); 
				System.out.println("AI score is: "+aiplayer); 				
				System.out.println("Human score is: "+humanplayer); 
				break;
			}
        		}
	}			
	//for the one-move game mode	
	public void gameplaystart1(gameplayboard g,String fileoutput) throws Exception
	{
		if(genericcheck(g))
		{
			System.out.println("Board is full");
			int finalgameboardresult=finalgameboardresult(g);
			System.out.println("Ai score is:"+aiplayer);
			System.out.println("Human score is:"+ humanplayer);
			System.exit(0);
		}
		else
		{
			g.outputgameboard();
			int finalgameboardresult=finalgameboardresult(g);
			System.out.println("Ai score is:"+aiplayer);
			System.out.println("Human score is:"+ humanplayer);
			System.out.println("Ai move:");
			g.playpiece(computerturn(),1);
			g.outputgameboard();
			finalgameboardresult=finalgameboardresult(g);
			System.out.println("Ai score is:"+aiplayer);
			System.out.println("Human score is:"+ humanplayer);
			BufferedWriter result = new BufferedWriter(new FileWriter(fileoutput));
            			for( int a = 0; a < 6; a++ )
			{
            				for( int b = 0; b < 7; b++ )
				{
					result.write(g.gameplayboard[a][b] + 48);
				}
				result.write("\r\n");
			}
			result.write("2" + "\r\n");
			result.close();
		}
	}

	public maxconnect4(gameplayboard g)
	{
		this.g = g;
		scan= new Scanner(System.in);
	}
	
	public void oppositionmove()
	{
		System.out.println(" Place your move(1-7; ");
		int gamemove =scan.nextInt();
		while(gamemove<1 || gamemove >7 || !g.isplaymovevalid(gamemove-1))
		{
			System.out.print(" Your move is Invalid. \n\n Place your move(1-7): ");
			gamemove=scan.nextInt();
		}
		g.playpiece(gamemove-1,2);
	}

	public boolean genericcheck(gameplayboard g)
	{
		for(int a=0;a<g.gameplayboard[0].length;a++)
		{
			if(g.gameplayboard[0][a]==0)
			return false;
		}
		return true;
	}
	//calculates the quatarpules score
	public int scorecalulation(int aiplayerposition, int movescount)
	{
		int turns = 4-movescount;
		if(aiplayerposition==0)
		return 0;
		else if(aiplayerposition==1)
		return turns;
		else if(aiplayerposition==2)
		return 5*turns;
		else if(aiplayerposition==3)
		return 8*turns;
		else return 999;
	}
	//calculates the final result for the game
	public int finalgameboardresult(gameplayboard  g)
	{
		int aiplayerscore=0;
		int humanplayerscore=0;
		aiplayer=0;
		humanplayer=0;
		for(int a=5;a>=0;--a)
		{
			for(int b=0;b<=6;++b)
			{
				if(g.gameplayboard[a][b]==0)
				continue;

				if(b<=3)
				{
					for(int t=0;t<4;++t)
					{
						if(g.gameplayboard[a][b+t]==1)
						aiplayerscore++;
						else if(g.gameplayboard[a][b+t]==2)
						humanplayerscore++;
						else
						break;
					}
					if(aiplayerscore==4)
					aiplayer++;
					else if(humanplayerscore==4)
					humanplayer++;
					aiplayerscore=0;
					humanplayerscore=0;
				}
				
				if(a>=3)
				{
					for(int t=0;t<4;++t)
					{
						if(g.gameplayboard[a-t][b]==1)
						aiplayerscore++;
						else if(g.gameplayboard[a-t][b]==2)
						humanplayerscore++;
						else 
						break;
					}
					if(aiplayerscore==4)
					aiplayer++;
					else if(humanplayerscore==4)
					humanplayer++;
					aiplayerscore=0;
					humanplayerscore=0;
				}	

				if(a>=3 && b<=3)
				{
					for(int t=0;t<4;++t)
					{
						if(g.gameplayboard[a-t][b+t]==1)
						aiplayerscore++;
						else if(g.gameplayboard[a-t][b+t]==2)
						humanplayerscore++;
						else
						break;
					}
					if(aiplayerscore==4)
					aiplayer++;
					else if(humanplayerscore==4)
					humanplayer++;
					aiplayerscore=0;
					humanplayerscore=0;
				}
				
				if(a>=3 && b>=3)
				{
					for(int t=0;t<4;++t)
					{
						if(g.gameplayboard[a-t][b-t]==1)
						aiplayerscore++;
						else if(g.gameplayboard[a-t][b-t]==2)
						humanplayerscore++;
						else
						break;
					}
					if(aiplayerscore==4)
					aiplayer++;
					else if(humanplayerscore==4)
					humanplayer++;
					aiplayerscore=0;
					humanplayerscore=0;
				}
			}
		}
		
		for(int b=0;b<7;++b)
		{
			if(g.gameplayboard[0][b]==0)
			return -1;
		}
		if(aiplayer>humanplayer)
		return 1;
		else if(humanplayer>aiplayer)
		return 2;
		else
		return 0;
	}
	//evaluation function for the game
	public int gameboardeval(gameplayboard g)
	{
		int blocksempty=0;
		int movescount=0;
		int scoreeval=0;
		int aiplayer1=1;
		int p=0;
		for( int a=5;a>=0;--a)
		{
			for(int b=0;b<=6;++b)
			{
				if(g.gameplayboard[a][b]==0 || g.gameplayboard[a][b]==2)
				continue;
		
				if(b<=3)
				{
					for(p=1;p<4;++p)
					{
						if(g.gameplayboard[a][b+p]==1)
						aiplayer1++;
						else if(g.gameplayboard[a][b+p]==2)
						{
							aiplayer1=0;
							blocksempty =0;
							break;
						}
						else blocksempty++;
					}
					movescount=0;
					if(blocksempty>0)
					for(int q=1;q<4;++q)
					{
						int c=b+q;	
						for(int r=a;r<=5;r++)
						{
							if(g.gameplayboard[r][c]==0)
							movescount++;
							else
							break;
						}
					}
					if(movescount!=0)
					scoreeval += scorecalulation(aiplayer1, movescount);
					aiplayer1=1;
					blocksempty=0;
				}

				if(a>=3)
                				{
                    				for(p=1;p<4;++p)
                   				 {
                        					if(g.gameplayboard[a-p][b]==1)
                            					aiplayer1++;
                        					else if(g.gameplayboard[a-p][b]==2)
                            					{
                                						aiplayer1=0;
                                						break;
                            					} 
                    				} 
                    				movescount = 0; 
                    
                    				if(aiplayer1>0)
                    				{
                        					int c = b;
                        					for(int r=a-p+1; r<=a-1;r++)
                       				 	{
                          						if(g.gameplayboard[r][c]==0)
                            						movescount++;
                            						else 
                                						break;
                        					}  
                    				}
                    				if(movescount!=0)
                     				scoreeval += scorecalulation(aiplayer1, movescount);
                    				aiplayer1=1;  
                    				blocksempty = 0;
               				 }
		
				 if(b>=3)
                				{
                    				for(p=1;p<4;++p)
                   	 			{
                        					if(g.gameplayboard[a][b-p]==1)
                            					aiplayer1++;
                        					else if(g.gameplayboard[a][b-p]==2)
                            					{
                                						aiplayer1=0;
                                						blocksempty=0;
                                						break;
                            					}
                        					else 
						blocksempty++;
                    				}
                    				movescount=0;
                    				if(blocksempty>0) 
                        				for(int q=1;q<4;++q)
                        				{
                            					int c = b- q;
                            					for(int r=a; r<= 5;r++)	
                            					{
                             						if(g.gameplayboard[r][c]==0)
                             						movescount++;
                                						else
                                 						break;
                            					} 
                        				} 
                    				if(movescount!=0)
                     				scoreeval+= scorecalulation(aiplayer1, movescount);
                    				aiplayer1=1; 
                   				blocksempty = 0;
                				}

				if(a>=3 && b<=3)
                				{
                    				for(p=1;p<4;++p)
                    				{
                        					if(g.gameplayboard[a-p][b+p]==1)
                            					aiplayer1++;
                        					else if(g.gameplayboard[a-p][b+p]==2)
                            					{
                                						aiplayer1=0;
                               						blocksempty=0;
                             	 	 	 			break;
                            					}
                        					else blocksempty++;                        
                   				 }
                    				movescount=0;
                    				if(blocksempty>0)
                    				{
                        				for(int q=1;q<4;++q)
                        				{	
                            					int c = b+q;
						int row = a-q;
                            					for(int r=row;r<=5;++r)
                            					{
                              						if(g.gameplayboard[r][c]==0)
                                    					movescount++;
                                						else if(g.gameplayboard[r][c]==1);
                                						else
							break;
                            					}
                        				} 
                        				if(movescount!=0) 
                            				scoreeval += scorecalulation(aiplayer1, movescount);
                        				aiplayer1=1;
                        				blocksempty = 0;
                    			 	}
               				 }
                 
                				if(a>=3 && b>=3)
                				{
                    				for(p=1;p<4;++p)
                    				{
                        					if(g.gameplayboard[a-p][b-p]==1)
                            					aiplayer1++;
                        					else if(g.gameplayboard[a-p][b-p]==2)
                            					{
                                						aiplayer1=0;
                               			 			blocksempty=0;
                                						break;
                            					}
                        					else blocksempty++;                        
                    				}
                    				movescount=0;
                    				if(blocksempty>0)
                    				{
                       				for(int q=1;q<4;++q)
                        				{
                            					int c = b-q; 
	          					int row = a-q;
                            					for(int r=row;r<=5;++r)
                            					{
                                						if(g.gameplayboard[r][c]==0)
                                    					movescount++;
                                						else if(g.gameplayboard[r][c]==1);
                                						else 
							break;
                            					}
                        				} 
                        				if(movescount!=0)
                         				scoreeval+= scorecalulation(aiplayer1, movescount);
                        				aiplayer1=1;
                     	   			blocksempty = 0;
                    				}
				}
			}
		}
		return scoreeval;
	}
	//depth limited minmax using alpha-beta pruning 
	public int abminmax(int maxdepth, int playerchance, int alp, int bet)
	{
		boolean maxdepthcondition=true;
		if(maxdepthcondition)
		{
			maxdepthcondition=false;
			if(maxdepthlimit==0)
			{
				System.out.println("Atleast 1 should be depthlimit");
				System.exit(0);
			}
		}
		if(bet<=alp)
		{
			if(playerchance ==1)
			return Integer.MAX_VALUE;
			else
			return Integer.MIN_VALUE;
		}
		int finalgameboardresult=finalgameboardresult(g);
		if(finalgameboardresult==1)
		return Integer.MAX_VALUE/3;
		else if(finalgameboardresult==2)
		return Integer.MIN_VALUE/3;
		else if(finalgameboardresult==0)
		return 0;
		if(maxdepth==maxdepthlimit)
		return gameboardeval(g);
		int scoremax=Integer.MIN_VALUE;
		int scoremin=Integer.MAX_VALUE;
		for(int b=0;b<=6;++b)
		{
			int scorecurrent=0;
			if(!g.isplaymovevalid(b))
			continue;
			if(playerchance==1)
			{
				g.playpiece(b,1);
				scorecurrent = abminmax(maxdepth+1,2,alp,bet);
				if(maxdepth==0)
				{
					if(scorecurrent > scoremax)
					nextplayermove= b;
					if(scorecurrent==Integer.MAX_VALUE/3)
					{
						g.removeapiece(b);
						break;
					}
				}
				scoremax=Math.max(scorecurrent,scoremax);
				alp=Math.max(scorecurrent,alp);
			}
			else if(playerchance==2)
			{
				g.playpiece(b,2);
				scorecurrent=abminmax(maxdepth+1,1,alp,bet);
				scoremin=Math.min(scorecurrent,scoremin);
				bet=Math.min(scorecurrent,bet);
			}
			g.removeapiece(b);
			if(scorecurrent == Integer.MAX_VALUE || scorecurrent == Integer.MIN_VALUE)
			break;
		}
		return playerchance==1?scoremax:scoremin;
	}
	
	public int computerturn()
	{
		nextplayermove=-1;
		abminmax(0,1, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return nextplayermove;
	}
	//initial game board state
	public boolean initialboardstate(gameplayboard g)
	{
		for(int a=0;a<g.gameplayboard[5].length;a++)
		{
			if(g.gameplayboard[5][a]!=0)
			return false;
		}
		return true;
	}
	//prints the output into a file
	public void outputfile(gameplayboard g,int gameturn) throws Exception
	{
		if(gameturn==2)
		{
			BufferedWriter result= new BufferedWriter(new FileWriter("human.txt"));
			for(int a=0;a<6;a++)
			{	
				for(int b=0;b<7;b++)
				{
					result.write(g.gameplayboard[a][b] + 48);
				}					
				result.write("\r\n");
			}
			//output current turn  		
			result.write("1" + "\r\n");
			result.close();
		}
		else
		{
			BufferedWriter result=new BufferedWriter(new FileWriter("computer.txt"));
			for(int a=0;a<6;a++)
			{
				for(int b=0;b<7;b++)
				{
					result.write(g.gameplayboard[a][b] + 48);
				}
				result.write("\r\n");
			}
			result.write("2" + "\r\n");
			result.close();
		}
	}

	

}
	
			
				
