package org.sbrubles.zcontainer.core;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.sbrubles.zcontainer.api.Container;
import org.sbrubles.zcontainer.api.deployers.Deployer;

public class DirectoryDeployer extends Deployer {

	@Override
	public void init(Container container) {
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		new DirectoryDeployer().watchDir("/home/blah/tmp");
	}

	public static void watchDir(String dir) throws IOException, InterruptedException{

		//create the watchService
		final WatchService watchService = FileSystems.getDefault().newWatchService();

		//register the directory with the watchService
		//for create, modify and delete events
		final Path path = Paths.get(dir);
		path.register(watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE);

		//start an infinite loop
		while(true){

			//remove the next watch key
			final WatchKey key = watchService.take();

			//get list of events for the watch key
			for (WatchEvent<?> watchEvent : key.pollEvents()) {

				//get the filename for the event
				final WatchEvent<Path> ev = (WatchEvent<Path>)watchEvent;
				final Path filename = ev.context();

				//get the kind of event (create, modify, delete)
				final Kind<?> kind = watchEvent.kind();

				kind.equals(StandardWatchEventKinds.ENTRY_CREATE);
				//print it out
				System.out.println(kind + ": " + filename.toAbsolutePath());
			}

			if (!key.reset()) {
				break;
			}
		}
	}

}
