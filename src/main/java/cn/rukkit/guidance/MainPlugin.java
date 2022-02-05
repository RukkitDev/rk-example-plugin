package cn.rukkit.guidance;

import cn.rukkit.event.EventHandler;
import cn.rukkit.event.EventListener;
import cn.rukkit.event.player.PlayerJoinEvent;
import cn.rukkit.plugin.RukkitPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.slf4j.Logger;

public class MainPlugin extends RukkitPlugin implements EventListener{
	
	private GuideConfig config;
	private Logger log = getLogger();
	
	private final void loadConfig() throws IOException {
		if (config != null) return;
		Yaml yaml = new Yaml(new CustomClassLoaderConstructor(GuideConfig.class, new GuideConfig().getClass().getClassLoader()));
		File confFile = getConfigFile("uplist");
		if (confFile.exists() && confFile.isFile() && confFile.length() > 0) {
			getLogger().debug("Found Config file.Reading...");
		} else {
			getLogger().debug("Config file.not found.Creating...");
			confFile.delete();
			confFile.createNewFile();
			FileWriter writer = new FileWriter(confFile);
			writer.write(yaml.dumpAs(new GuideConfig(), null, DumperOptions.FlowStyle.BLOCK));
			writer.flush();
			writer.close();
		}
		config = yaml.loadAs((new FileInputStream(confFile)), new GuideConfig().getClass());
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		event.getPlayer().getConnection().sendServerMessage(config.guideMessage);
	}
	
	@Override
	public void onLoad() {
		log.info("GuidePlugin:init");
		try {
			loadConfig();
		} catch (IOException e) {
			throw new RuntimeException("Config load failed!");
		}
		getPluginManager().registerEventListener(this, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onDone() {
	}
}
