package com.src.irods;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.IRODSSession;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.IRODSFileSystemAOImpl;
import org.irods.jargon.core.pub.UserAO;
import org.irods.jargon.core.pub.domain.User;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileFactory;
public class Connection_iRODS {

	public static final String DATA_IPLANTCOLLABORATIVE_ORG = "data.iplantcollaborative.org";
	public static String HOME_DIR ="/iplant/home/sharanbabuk";
	public static String ZONE="iplant";
	public static final String DEFAULT_STORAGE_RESOURCE = null;
	
	public static String FILE_PATH = "/iplant/home/sharanbabuk/TestImages/SpitzerTelescope_PinWheelGalaxy.jpg";
	public static String TEST_IMAGES_DIR_PATH = "/iplant/home/sharanbabuk/TestImages";
	public static String HOME_DIR_PATH = "/iplant/home/sharanbabuk/";
	public static String FILE_PATH_WITHOUT_IPLANT = "/home/sharanbabuk/TestImages/SpitzerTelescope_PinWheelGalaxy.jpg";
	public static String LOCAL_FILE = "D:/iRODS/Images/bio5_header.jpeg";
	public static void main(String args[]) throws IOException{
		
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter your username:/n");
		String userName =br1.readLine() ;
		System.out.println("Enter your password:/n");
		String password =br2.readLine();
		
		IRODSAccount iRODSAccount = new IRODSAccount ( 
				DATA_IPLANTCOLLABORATIVE_ORG, 1247, userName, password, HOME_DIR, ZONE, DEFAULT_STORAGE_RESOURCE );
		IRODSFileSystem irodsFileSystem = null;


		try {
			irodsFileSystem = IRODSFileSystem.instance();

			UserAO  userAccount = irodsFileSystem.getIRODSAccessObjectFactory().getUserAO(iRODSAccount);
			IRODSSession iRODSSession =irodsFileSystem.getIrodsSession();
			IRODSAccessObjectFactory iRODSAccessObjectFactory =irodsFileSystem.getIRODSAccessObjectFactory();
			IRODSFileFactory iRODSFileFactory =irodsFileSystem.getIRODSFileFactory(iRODSAccount);

			/*
			 * Account Details*/
			System.out.println("Welcome "+ iRODSAccount.getUserName() +"..!");

			/*
			 * Server Properties*/
			System.out.println("iRODS Zone: " +userAccount.getIRODSServerProperties().getRodsZone());

			IRODSFile iRodsFile =iRODSFileFactory.instanceIRODSFile(TEST_IMAGES_DIR_PATH);
			List<User> users=userAccount.findAll();
			System.out.println("Users Logged in current Zone: " +users.size());


			/*
			 * Print User details */
			PrintWriter outputFile = new PrintWriter (new FileWriter("D:\\iRODS\\OutputFiles\\irods_user_list.txt"));
			Iterator<User> iterator=users.iterator();
			while(iterator.hasNext()){
				//System.out.println(iterator.next());
				outputFile.print(iterator.next());
			}
			
			/*
			 * File Operations*/

			IRODSFileSystemAOImpl IRODSFileSystemAOImpl  =new IRODSFileSystemAOImpl(iRODSSession, iRODSAccount);
			List<String> listInDir  = IRODSFileSystemAOImpl.getListInDir(iRodsFile);

			Iterator<String> listInDirectory =listInDir.iterator();
			int count = 1;
			while(listInDirectory.hasNext())
			{
				System.out.println("Files in Dir:" +count +" " +listInDirectory.next());
				count++;
			}

			/*
			 * File Permissions*/
			System.out.println("File permissions :" +IRODSFileSystemAOImpl.getDirectoryPermissions(iRodsFile));

			irodsFileSystem.close();
		} catch (JargonException jargonException) {
			jargonException.printStackTrace();
		}
	}
}
