package com.helpfinder.model;

import java.io.Serializable;

// this class represents the worker user preference to show instruction
public class WorkerSearchPreference extends SearchPreference implements Serializable {    
    private static final long serialVersionUID = 1L;
    // instruction to show if the user is discovered in a search with-in the search preference and skill set
    public String Instruction;
    
    public String getInstruction() {
        return Instruction;
    }
    
    public void setInstruction(String Instruction) {
        this.Instruction = Instruction;
    }
}
