
# >>> conda initialize >>>
# !! Contents within this block are managed by 'conda init' !!
__conda_setup="$('/Users/shaojie/opt/anaconda3/bin/conda' 'shell.zsh' 'hook' 2> /dev/null)"
if [ $? -eq 0 ]; then
    eval "$__conda_setup"
else
    if [ -f "/Users/shaojie/opt/anaconda3/etc/profile.d/conda.sh" ]; then
        . "/Users/shaojie/opt/anaconda3/etc/profile.d/conda.sh"
    else
        export PATH="/Users/shaojie/opt/anaconda3/bin:$PATH"
    fi
fi
unset __conda_setup
# <<< conda initialize <<<
export /Users/shaojie/ANTLR/antlr-4.13.1-complete.jar
alias antlr4='java -jar $ANTLR4'

