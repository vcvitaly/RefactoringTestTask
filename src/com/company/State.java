package com.company;

import java.util.Objects;

// TODO remove since it's not needed?
public class State {
    private String stateCode;
    private String stateName;

    public State(String stateCode, String stateName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(stateCode, state.stateCode) && Objects.equals(stateName, state.stateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateCode, stateName);
    }
}
