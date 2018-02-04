import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class FileSorter
{
	private static final String FILENAME_SWITCH = "-fn";
	private static final int ACTIVITY_FREQUENCY = 100;
	
	private static String activityIcon = "-";
	private static int activityCounter = ACTIVITY_FREQUENCY;
	
	private static long lastActivityChange = 0;
	
	public static void main(String args[])
	{
		if(args.length < 3)
		{
			System.out.println("Wrong number of arguments.");
		}
		else
		{
			String searchDirStr = args[0];
			String matchDirStr	= args[1];
			String searchTerm 	= args[2].toLowerCase();
			
			String optionSwitch = "NONE";
			if(args.length > 3)
			{
				optionSwitch = args[3];
			}
			File matchDir 	= new File(matchDirStr);
			File searchDir 	= new File(searchDirStr);
			
			if(!searchDir.isDirectory())
			{
				System.out.println("\"" + searchDirStr + "\" is not a directory!");
			}
			else if(!matchDir.isDirectory())
			{
				System.out.println("\"" + matchDirStr + "\" is not a directory!");
			}
			else
			{
				ArrayList<File> fileList = findFiles(searchDirStr, null);
				
				System.out.println("Searching for \"" + searchTerm + "\" in " + fileList.size() + " files.");
				
				Scanner in = new Scanner(System.in);
				
				String userInput = "";
				while(!userInput.equals("y") && !userInput.equals("n"))
				{
					System.out.println("Continue search (Y/N)?");
					userInput = in.nextLine();
					userInput = userInput.toLowerCase();
				}
				
				if(userInput.equals("y"))
				{
					int matchesFound = 0;
					long searchStartTime = System.currentTimeMillis();
					int fileListSize = fileList.size();
					for(int i = 0; i < fileListSize; i++)
					{
						boolean fileNameSwitch = optionSwitch.equalsIgnoreCase(FILENAME_SWITCH);
						if(!fileNameSwitch)
						{
							try
							{
								File path = fileList.get(i);
								BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
								StringBuilder sb = new StringBuilder((int)path.length());
						
								int c = 0;
							
								while((c = bis.read()) != -1)
								{
									sb.append((char)c);
								}
								
								if(sb.toString().toLowerCase().indexOf(searchTerm) != -1)
								{
									matchesFound++;
									String fileName = path.getName();
									String newFileName = matchDirStr + "\\" + fileName;
									System.out.println("Making copy of " + fileName);
									copy(path.getPath(), newFileName);
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						if(fileNameSwitch)
						{
							File path = fileList.get(i);
							String fileName = path.getName();
							if(fileName.toLowerCase().indexOf(searchTerm) != -1)
							{
								matchesFound++;
								String newFileName = matchDirStr + "\\" + fileName;
								System.out.println("Making copy of " + fileName);
								copy(path.getPath(), newFileName);
							}
						}
						showProgress(i, fileListSize);
					}
					long searchTimeInSeconds = (System.currentTimeMillis() - searchStartTime)/1000;
					if(searchTimeInSeconds == 0)
					{
						searchTimeInSeconds = 1;
					}
					long filesPerSecond = fileListSize/searchTimeInSeconds;
					System.out.println("");
					System.out.println(fileListSize + " files searched in " + searchTimeInSeconds + " seconds or " + filesPerSecond + "fps.");
				}
			}
			
		}
	}
	
	private static void showActivityTwo()
	{
		long timeLapse = System.currentTimeMillis() - lastActivityChange;
		if(timeLapse >= ACTIVITY_FREQUENCY)
		{
			System.out.print(activityIcon + "\r");
			updateActivityIcon();
		}
	}
	
	private static void showActivity()
	{
		if(activityCounter++ >= ACTIVITY_FREQUENCY)
		{
			System.out.print(activityIcon + "\r");
			updateActivityIcon();
		}
	}
	
	private static void updateActivityIcon()
	{
		if(activityIcon.equals("-"))
		{
			activityIcon = "\\";
		}
		else if(activityIcon.equals("\\"))
		{
			activityIcon = "|";
		}
		else if(activityIcon.equals("|"))
		{
			activityIcon = "/";
		}
		else
		{
			activityIcon = "-";
		}
		lastActivityChange = System.currentTimeMillis();
	}
	
	private static void showProgress(int completed, int total)
	{
		long timeLapse = System.currentTimeMillis() - lastActivityChange;
		if(timeLapse >= ACTIVITY_FREQUENCY)
		{
			final int BAR_SIZE = 75;
			completed += 1;
			int percent = (int)(((float)completed/(float)total) * BAR_SIZE);

			String bar = "|";
			for(int i = 0; i < percent; i++)
			{
				bar += "=";
			}
			bar += activityIcon;
			for(int j = percent; j < BAR_SIZE-1; j++)
			{
				bar += " ";
			}
			bar += "|\r";
			System.out.print(bar);
			updateActivityIcon();
		}
	}
	
	public static ArrayList<File> findFiles(String directory, ArrayList<File> fileList)
	{
		if(fileList == null)
		{
			fileList = new ArrayList();
		}
	
		File file = new File(directory);
		File list[] = file.listFiles();
		
		if(list != null)
		{
			for(int i = 0; i < list.length; i++)
			{
				File tmp = list[i];
				if(tmp.isFile())
				{
						fileList.add(tmp);
				}
				else if(tmp.isDirectory())
				{
					findFiles(tmp.getPath(), fileList);
				}
				// showActivity();
				showActivityTwo();
			}
		}
		return fileList;
	}
	
	public static void copy(String a, String b)
	{
		InputStream inStream = null;
		OutputStream outStream = null;
 
    	try{
 
			File afile = new File(a);
			File bfile = new File(b);

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			
			while ((length = inStream.read(buffer)) > 0){

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			bfile.setLastModified(afile.lastModified());
			
			// System.out.println("-File copied successful!");
 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
}