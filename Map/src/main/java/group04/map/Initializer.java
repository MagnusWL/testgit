package group04.map;

import org.openide.util.lookup.ServiceProvider;
import group04.common.Entity;
import group04.common.EntityType;
import group04.common.GameData;
import group04.common.World;
import group04.common.services.IServiceInitializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServiceProvider(service = IServiceInitializer.class)

public class Initializer implements IServiceInitializer {

    private Entity map;

    public Initializer() {
    }

    @Override
    public void start(GameData gameData, World world) {
//        map = generateMap(gameData);
        map = loadMap(gameData);
        world.addEntity(map);
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private Entity loadMap(GameData gameData) {

        FileInputStream fin = null;
        ObjectInputStream ois = null;
        try {
            fin = new FileInputStream(new File("../../../Common/src/main/resources/map.object").getAbsolutePath());
            ois = new ObjectInputStream(fin);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        Entity newMap = null;

        if (ois != null) {
            int[][] newMapInt;

            try {
                newMapInt = (int[][]) ois.readObject();
                gameData.setMapHeight(newMapInt[0].length);
                gameData.setMapWidth(newMapInt.length);
                newMap = new Entity();
                newMap.setEntityType(EntityType.MAP);
                newMap.setMap(newMapInt);
            } catch (IOException ex) {
                Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Initializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (newMap != null) {
            return newMap;
        }

        return generateMap(gameData);
    }

    private Entity generateMap(GameData gameData) {
        int[][] newMapInt = new int[gameData.getMapWidth()][gameData.getMapHeight()];

        for (int i = 0; i < gameData.getMapWidth(); i++) {
            for (int j = 0; j < gameData.getMapHeight(); j++) {
                if (j > (int) (gameData.getMapHeight() * 0.13)) {
                    newMapInt[i][j] = 0;
                } else {
                    newMapInt[i][j] = 1;
                }
            }
        }

        Entity newMap = new Entity();
        newMap.setEntityType(EntityType.MAP);
        newMap.setMap(newMapInt);
        return newMap;
    }
}