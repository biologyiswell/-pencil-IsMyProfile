/**
 * MIT License
 *
 * Copyright (c) 2017 biologyiswell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.biologyiswell.profiler;

import com.github.biologyiswell.profiler.command.IsMyProfilerCmd;
import com.github.biologyiswell.profiler.listeners.PlayerListener;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by biologyiswell on 03/10/2017.
 */
public class IsMyProfiler extends JavaPlugin
{
    /** Instance, this represents the isntance from IsMyProfiler plugin */
    public static IsMyProfiler instance;

    /** Gson */
    public static final Gson GSON = new Gson();

    /** Executor Services, this runnable a async tasks */
    public static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    /**
     * On Enable,
     * This method is called when plugin is call to enable
     */
    @Override
    public void onEnable()
    {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("profiler").setExecutor(new IsMyProfilerCmd());

        getLogger().info("IsMyProfiler enabled.");
    }

    /**
     * On Disable,
     * This method is called when plugin is call to disable
     */
    @Override
    public void onDisable()
    {
        getLogger().info("IsMyProfiler disabled.");
    }
}
