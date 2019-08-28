/*
 *    Copyright  2017 Denis Kokorin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.sombrainc.ffmpegtool.modeling;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public enum SizeUnit {
    /**
     * kilobit
     */
    K(1_000L),
    /**
     * megabit
     */
    M(1_000_000L),
    /**
     * gigabit
     */
    G(1_000_000_000L),

    Ki(1_024L),
    Mi(1_024L * 1_024),
    Gi(1_024L * 1_024 * 1_024),

    /**
     * byte
     */
    B(8L),
    /**
     * kilobyte
     */
    KB(1_000L * 8),
    /**
     * megabyte
     */
    MB(1_000_000L * 8),
    /**
     * gigabyte
     */
    GB(1_000_000_000L * 8),

    KiB(1_024L * 8),
    MiB(1_024L * 1_024 * 8),
    GiB(1_024L * 1_024 * 1_024 * 8),
    ;
    private long multiplier;

    SizeUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    public long multiplier() {
        return multiplier;
    }

    public long convertTo(long value, SizeUnit unit) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(this.multiplier))
                .divide(BigDecimal.valueOf(unit.multiplier), RoundingMode.CEILING)
                .longValue();
    }

    public long toBytes(long value) {
        return convertTo(value, B);
    }

    public static Optional<SizeUnit> of(String value) {
        for (SizeUnit unit : SizeUnit.values()) {
            if (unit.name().equalsIgnoreCase(value)) {
                return Optional.of(unit);
            }
        }

        return Optional.empty();
    }
}
