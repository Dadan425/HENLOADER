package org.bdj;

import java.io.*;
import java.util.*;
import javax.tv.xlet.*;
import java.awt.BorderLayout;
import org.havi.ui.HScene;
import org.havi.ui.HSceneFactory;
import org.dvb.event.UserEvent;
import org.dvb.event.EventManager;
import org.dvb.event.UserEventListener;
import org.dvb.event.UserEventRepository;
import org.bluray.ui.event.HRcEvent;
import org.bdj.sandbox.DisableSecurityManagerAction;
import org.bdj.external.*;

public class InitXlet implements Xlet, UserEventListener
{
    public static final int BUTTON_X = 10;
    public static final int BUTTON_O = 19;
    public static final int BUTTON_U = 38;
    public static final int BUTTON_D = 40;
    private static InitXlet instance;
    
    public static class EventQueue
    {
        private LinkedList l;
        int cnt = 0;
        EventQueue()
        {
            l = new LinkedList();
        }
        public synchronized void put(Object obj)
        {
            l.addLast(obj);
            cnt++;
        }
        public synchronized Object get()
        {
            if(cnt == 0)
                return null;
            Object o = l.getFirst();
            l.removeFirst();
            cnt--;
            return o;
        }
    }
    
    private EventQueue eq;
    private HScene scene;
    private Screen gui;
    private XletContext context;
    private static PrintStream console;
    private static final ArrayList messages = new ArrayList();
    
    public void initXlet(XletContext context)
    {
        // Privilege escalation
        try {
            DisableSecurityManagerAction.execute();
        } catch (Exception e) {}

        instance = this;
        this.context = context;
        this.eq = new EventQueue();
        scene = HSceneFactory.getInstance().getDefaultHScene();
        try
        {
            gui = new Screen(messages);
            gui.setSize(1920, 1080); // BD screen size
            scene.add(gui, BorderLayout.CENTER);
            UserEventRepository repo = new UserEventRepository("input");
            repo.addKey(BUTTON_X);
            repo.addKey(BUTTON_O);
            repo.addKey(BUTTON_U);
            repo.addKey(BUTTON_D);
            EventManager.getInstance().addUserEventListener(this, repo);
            (new Thread()
            {
                public void run()
                {
                    try
                    {
                        scene.repaint();
                        console = new PrintStream(new MessagesOutputStream(messages, scene));
                        
                        // --- SUSUNAN TEKS DISAMAKAN DENGAN REFERENSI ---
                        console.println("* Follow Tiktok D4nzxml *");
                        console.println("HenLoader Auto Poops JB 9.00 - 12.50 - 12.52 Based on:");
                        console.println("  - HenLoader LP v1.0 by 15432");
                        console.println("  - GoldHEN 2.4b18.7 by SiSTR0");
                        console.println("  - poops code by theflow0");
                        console.println("  - BDJ build environment by kimariin");
                        console.println("  - java console by sleirsgoevy");
                        console.println("  - Modded & Design by D4nzxml");
                        // -----------------------------------------------
                        
                        System.gc(); // this workaround somehow makes Call API working
                        if (System.getSecurityManager() != null) {
                            console.println("Priviledge escalation failure, unsupported firmware?");
                        } else {
                            Kernel.initializeKernelOffsets();
                            String fw = Helper.getCurrentFirmwareVersion();
                            
                            // Teks deteksi firmware diubah sesuai referensi
                            console.println("Your Playsation Firmware : " + fw);
                            
                            if (!KernelOffset.hasPS4Offsets())
                            {
                                // Teks gagal diubah sesuai referensi
                                console.println("Your Playstation Firmware Is Not Supported!");
                            } else {
                                int result = org.bdj.external.Poops.main(console);
                                if (result == 0)
                                {
                                    // Teks sukses diubah sesuai referensi
                                    console.println("Berhasil Bro Selamat !! ");
                                    console.println("PS4 Anda sekarang sudah di-jailbreak. Semoga Harimu Senin Selalu!");
                                    
                                    // --- TAMBAHAN FITUR NOTIFIKASI PS4 ---
                                    try {
                                        org.bdj.api.NativeInvoke.sendNotificationRequest("Your PS4 Now Jailbroken Have Fun! - D4nzxml");
                                    } catch (Throwable t) {
                                        // Abaikan jika API tidak tersedia
                                    }
                                    // -------------------------------------
                                } else {
                                    console.println("Fatal fail(" + result + "), please REBOOT PS4");
                                }
                            }
                        }
                    }
                    catch(Throwable e)
                    {
                        scene.repaint();
                    }
                }
            }).start();
        }
        catch(Throwable e)
        {
            printStackTrace(e);
        }
        scene.validate();
    }
    
    public void startXlet()
    {
        gui.setVisible(true);
        scene.setVisible(true);
        gui.requestFocus();
    }
    public void pauseXlet()
    {
        gui.setVisible(false);
    }
    public void destroyXlet(boolean unconditional)
    {
        scene.remove(gui);
        scene = null;
    }
    private void printStackTrace(Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        if (console != null)
            console.print(sw.toString());
    }
    public void userEventReceived(UserEvent evt)
    {
        boolean ret = false;
        if(evt.getType() == HRcEvent.KEY_PRESSED)
        {
            ret = true;
            if(evt.getCode() == BUTTON_U)
                gui.top += 270;
            else if(evt.getCode() == BUTTON_D)
                gui.top -= 270;
            else
                ret = false;
            scene.repaint();
        }
        if(ret)
            return;
        if(evt.getType() == HRcEvent.KEY_PRESSED)
            eq.put(new Integer(evt.getCode()));
    }
    public static void repaint()
    {
        instance.scene.repaint();
    }
    public static int pollInput()
    {
        Object ans = instance.eq.get();
        if(ans == null)
            return 0;
        return ((Integer)ans).intValue();
    }
}
