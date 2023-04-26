package com.caspian.api.config;

import com.caspian.Caspian;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Container for {@link Config} backed by a {@link ConcurrentMap}. Manages
 * all declared configurations for the container class.
 *
 * @author linus
 * @since 1.0
 *
 * @see Config
 * @see ConfigFactory
 */
public class ConfigContainer implements Configurable
{
    // Container name is its UNIQUE identifier.
    private final String name;

    // List of all configurations in the container. The configs are managed
    // by a Map with references to their data tags.
    private final Map<String, Config<?>> configurations =
            Collections.synchronizedMap(new HashMap<>());

    /**
     * Uses the reflection {@link ConfigFactory} to add all declared configurations
     * to the config {@link ConcurrentMap}. Declared {@link Config}s will not
     * be registered if this process does not complete.
     *
     * @param name The container name
     *
     * @see ConfigFactory
     */
    public ConfigContainer(String name)
    {
        // populate container using reflection
        ConfigFactory factory = new ConfigFactory();
        for (Field field : getClass().getDeclaringClass().getDeclaredFields())
        {
            if (Config.class.isAssignableFrom(field.getType()))
            {
                Config<?> product = factory.build(field);
                product.setContainer(this);
                configurations.put(product.getRef(), product);
            }
        }
        this.name = name;
    }

    /**
     * Returns the container as a {@link JsonObject} containing a list of the
     * registered {@link Config}
     *
     * @return The container as a json object
     *
     * @see Config#toJson()
     */
    @Override
    public JsonObject toJson()
    {
        JsonObject out = new JsonObject();
        for (Config<?> config : getConfigs())
        {
            out.add(config.getRef(), config.toJson());
        }

        return out;
    }

    /**
     * Reads the configuration values from a {@link JsonObject} and updates
     * the {@link Config} values.
     *
     * @param jsonObj The container as a json object
     *
     * @see Config#fromJson(JsonObject)
     */
    @Override
    public void fromJson(JsonObject jsonObj)
    {
        for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet())
        {
            // config from ref
            Config<?> config = getConfig(entry.getKey());
            if (config != null)
            {
                try
                {
                    config.fromJson(entry.getValue().getAsJsonObject());
                }

                // couldn't parse Json value
                catch (Exception e)
                {
                    Caspian.error("Couldn't parse Json for " + name);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the container unique name identifier. This name will be
     * displayed to the users when representing this container.
     *
     * @return The unique name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the {@link Config} from the reference {@link Config#getRef()}
     * data tag in the registry.
     *
     * @param ref The config data tag
     * @return The config from the reference data tag
     *
     * @see Config#getRef()
     */
    public Config<?> getConfig(String ref)
    {
        return configurations.get(ref);
    }

    /**
     *
     *
     * @return
     */
    public Collection<Config<?>> getConfigs()
    {
        return configurations.values();
    }
}
