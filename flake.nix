{
  description = "Minecraft mod development flake";

  inputs = {
    nixpkgs.url = "https://channels.nixos.org/nixpkgs-unstable/nixexprs.tar.zst";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in {
      devShells.x86_64-linux.default = pkgs.mkShell rec {
        buildInputs = with pkgs; [
          # deps copied from prismlauncher's package
          # https://github.com/NixOS/nixpkgs/blob/nixos-26.05/pkgs/by-name/pr/prismlauncher/package.nix
          glfw3-minecraft
          openal

          ## openal
          alsa-lib
          libjack2
          libpulseaudio
          pipewire

          ## glfw
          libGL
          libx11
          libxcursor
          libxext
          libxrandr
          libxxf86vm
          wayland

          udev # oshi

          vulkan-loader # VulkanMod's lwjgl
          
          # might need for later
          # https://github.com/henriquekirchheck/minecraft-mod-dev-flake/blob/main/template/flake.nix
        ];

        shellHook = ''
          echo "Minecraft mod development environment enabled!"
        '';

        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath (buildInputs);
      };
    };
}

# vim: ts=2 sw=2 expandtab
