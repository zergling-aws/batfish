package org.batfish.vendor.sonic.representation;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.batfish.common.Warnings;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.Ip6;

/** Represents information in resolve.conf file that is provided as part of the SONiC file bundle */
public class ResolveConf implements Serializable {

  private @Nonnull final List<Ip> _nameservers;
  private @Nonnull final List<Ip6> _nameservers6;

  private ResolveConf(List<Ip> nameservers, List<Ip6> nameservers6) {
    _nameservers = nameservers;
    _nameservers6 = nameservers6;
  }

  public @Nonnull static ResolveConf deserialize(String resolveConfText, Warnings warnings) {
    ImmutableList.Builder<Ip> nameservers = ImmutableList.builder();
    ImmutableList.Builder<Ip6> nameservers6 = ImmutableList.builder();
    boolean foundNameserverLine = false;
    String[] lines = resolveConfText.split("\n");
    for (String line : lines) {
      Matcher matcher = NAMESERVER_LINE.matcher(line);
      if (matcher.matches()) {
        foundNameserverLine = true;
        String address = matcher.group(1);
        Optional<Ip> ip = Ip.tryParse(address);
        if (ip.isPresent()) {
          nameservers.add(ip.get());
          continue;
        }
        Optional<Ip6> ip6 = Ip6.tryParse(address);
        if (ip6.isPresent()) {
          nameservers6.add(ip6.get());
          continue;
        }
        warnings.redFlag(String.format("'%s' is neither IPv4 nor IPv6 address", address));
      }
    }
    if (!foundNameserverLine) {
      warnings.redFlag("No nameserver found");
    }
    return new ResolveConf(nameservers.build(), nameservers6.build());
  }

  @Nonnull
  public List<Ip> getNameservers() {
    return _nameservers;
  }

  @Nonnull
  public List<Ip6> getNameservers6() {
    return _nameservers6;
  }

  private static final Pattern NAMESERVER_LINE =
      Pattern.compile("^\\s*nameserver\\s+([^#\\s]+)\\s*(#.*)?$");
}
