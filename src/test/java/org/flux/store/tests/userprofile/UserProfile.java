package org.flux.store.tests.userprofile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Wither;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.flux.store.api.State;

@Getter
@Setter
@AllArgsConstructor
@With
public class UserProfile implements State {
    private String name;
    private String email;

    @Override
    public UserProfile clone() {
        try {
            return (UserProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
