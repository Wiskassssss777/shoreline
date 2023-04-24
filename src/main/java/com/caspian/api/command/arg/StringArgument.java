package com.caspian.api.command.arg;

import com.caspian.api.command.Command;

import java.util.*;

/**
 *
 *
 * @author linus
 * @since 1.0
 */
public class StringArgument extends Argument<String>
{
    private final Set<String> suggestions;

    public StringArgument(String... suggestions)
    {
        this.suggestions = new HashSet<>(Arrays.asList(suggestions));
    }

    public StringArgument(Collection<String> suggestions)
    {
        this.suggestions = new HashSet<>(suggestions);
    }

    /**
     * @see Command#runCommand()
     */
    @Override
    public void buildArgument()
    {
        String literal = getLiteral();
        if (suggestions.contains(literal))
        {
            setValue(literal);
        }
    }

    /**
     *
     *
     * @return
     */
    @Override
    public String[] getSuggestions()
    {
        return (String[]) suggestions.toArray();
    }
}
