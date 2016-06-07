package de.tomgrill.gdxtesting;




import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.gguy.game.estados.EstadosManager;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.obstaculos.WalkPlatform;


@RunWith(GdxTestRunner.class)
public class Testes {

    @Test
    public void testeFicheiroExiste() {
        FileHandle fs = Gdx.files.local("../android/assets/ficheiros/highscores.txt");
        assertTrue (fs.exists());
    }

    @Test
    public void badlogicLogoFileExists() {
        assertTrue("This test will only pass when the badlogic.jpg file coming with a new project setup has not been deleted.", Gdx.files
                .internal("../android/assets/badlogic.jpg").exists());
    }
    /** nao funcimina, nem sequer da para criar uma personagem de jogo
    @Test
    public void changeGravity(){
        EstadosManager emg = new EstadosManager();
        Guy gguy = new Guy(emg.skinSelected,0,0,0);
        gguy.changeGravity();
        assertTrue(gguy.isGuyFlying());
        assertTrue(gguy.normalGravity());
    }
    */
    /** rip
    @Test
    public void createAnObject(){
        WalkPlatform walkPlatform = new WalkPlatform(0);
        assertTrue(true);
    }
    */
    @Test
    public void TesteFicheirosMapa() {
        FileHandle fs = Gdx.files.local("../android/assets/map/base_color.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/coin.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/lock.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/obst.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/obst2.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/peixe.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/pokeball.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/question.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/sanic.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/turtle.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/walkplat.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/map/walkplat2.png");
        assertTrue (fs.exists());
    }
    @Test
    public void TesteFicheirosUtilidade() {
        FileHandle fs = Gdx.files.local("../android/assets/utility/backbutton.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/client.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/exit.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/mainmenu.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/options.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/powerbutton.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/restartbutton.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/server.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/singleplayer.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/slider_knob.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/slider_line.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/slider_background.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/utility/sound.png");
        assertTrue (fs.exists());
    }
    @Test
    public void testeMusicaExiste() {
        FileHandle sonic = Gdx.files.local("../android/assets/music/kendrick.mp3");
        assertTrue (sonic.exists());
        FileHandle res = Gdx.files.local("../android/assets/music/resonance.mp3");
        assertTrue (res.exists());
        FileHandle wal = Gdx.files.local("../android/assets/music/walk.mp3");
        assertTrue (wal.exists());
        FileHandle whistle = Gdx.files.local("../android/assets/music/whistle.mp3");
        assertTrue (whistle.exists());
        FileHandle kong = Gdx.files.local("../android/assets/music/kong.mp3");
        assertTrue (kong.exists());
    }
    @Test
    public void TesteFontFiles(){
        FileHandle fs = Gdx.files.local("../android/assets/dank.png");
        assertTrue (fs.exists());
        fs = Gdx.files.local("../android/assets/dank.fnt");
        assertTrue (fs.exists());
    }
    @Test
    public void TestColisao(){
        Rectangle r1 = new Rectangle(0,0,100,100);
        Rectangle r2 = new Rectangle(101,101,100,100);
        assertFalse(r1.overlaps(r2));
        r2.x-=2;
        r2.y-=2;
        assertTrue(r1.overlaps(r2));
    }
    @Test
    public void testeDefaultSkinExiste() {
        FileHandle iwalk = Gdx.files.local("../android/assets/sonic/invwalk.png");
        assertTrue (iwalk.exists());
        FileHandle walk = Gdx.files.local("../android/assets/sonic/walk.png");
        assertTrue (walk.exists());
        FileHandle jump = Gdx.files.local("../android/assets/sonic/jump.png");
        assertTrue (jump.exists());
        FileHandle sonic = Gdx.files.local("../android/assets/sonic/sonic.png");
        assertTrue (sonic.exists());
        FileHandle sound = Gdx.files.local("../android/assets/sounds/death.wav");
        assertTrue (sound.exists());
        sound = Gdx.files.local("../android/assets/sounds/jump.wav");
        assertTrue (sound.exists());
    }
}
