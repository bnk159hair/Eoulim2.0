import { useCallback, useEffect, useRef, useState } from 'react';
import { load } from '@tensorflow-models/facemesh';
import * as THREE from 'three';
import { useRecoilState } from 'recoil';
import { TRIANGULATION } from '../assets/data/triangulation';
import { uvs } from '../assets/data/frontProjectionUVMap';
import { positionBufferData } from '../assets/data/positionBufferData';
import { IsAnimonLoaded } from '../atoms/Session';

export const useFaceMeshModel = (): any => {
  const [model, setModel] = useState<any>(undefined);

  useEffect(() => {
    if (model) {
      return;
    }

    async function loadFacemesh() {
      const model = await load({
        maxContinuousChecks: 5,
        detectionConfidence: 0.9,
        maxFaces: 1,
        iouThreshold: 0.3,
        scoreThreshold: 0.75,
      });
      setModel(model);
    }
    loadFacemesh();
  }, [model]);

  return model;
};

export const useFaceMask = (
  videoElement: HTMLVideoElement | null,
  canvasElement: HTMLCanvasElement | null,
  avatarPath: any,
) => {
  const [model, setModel] = useState<any>();

  useEffect(() => {
    if (model) {
      return;
    }

    load({
      maxContinuousChecks: 5,
      detectionConfidence: 0.9,
      maxFaces: 1,
      iouThreshold: 0.3,
      scoreThreshold: 0.75,
    }).then(it => setModel(it));
  }, [model]);

  const requestRef = useRef<number>(0);
  const [faceCanvas, setFaceCanvas] = useState<FaceCanvas>();
  const [, setIsAnimonLoaded] = useRecoilState(IsAnimonLoaded);

  const animate = useCallback(async () => {
    if (!model) {
      return;
    }

    try {
      const predictions = await model!.estimateFaces(videoElement);
      if (!predictions.length) {
        requestRef.current = requestAnimationFrame(animate);
        return;
      }

      if (!faceCanvas) {
        setFaceCanvas(
          new FaceCanvas({
            canvas: canvasElement,
            textureFilePath: avatarPath,
            w: videoElement!.clientWidth,
            h: videoElement!.clientHeight,
          }),
        );
        setTimeout(() => {
          setIsAnimonLoaded(true);
        }, 2000);
        return;
      }

      const positionBufferData = predictions[0].scaledMesh.reduce(
        (acc: any, pos: any) => acc.concat(pos),
        [],
      );
      faceCanvas!.render(positionBufferData);
      requestRef.current = requestAnimationFrame(animate);
    } catch (e) {
      // console.log('이건 또 뭐야', e);
      requestRef.current = requestAnimationFrame(animate);
    }
  }, [model, videoElement, faceCanvas, canvasElement, avatarPath]);

  useEffect(() => {
    requestRef.current = requestAnimationFrame(animate);

    return () => cancelAnimationFrame(requestRef.current);
  }, [animate]);
};

class FaceCanvas {
  camera: any;

  halfW: any;

  halfH: any;

  scene: any;

  geometry: any;

  textureLoader: any;

  textureFilePath: any;

  material: any;

  mesh: any;

  renderer: any;

  static get EYEVERTICES() {
    return [
      // LEFT EYE
      133, 173, 157, 158, 159, 160, 161, 246, 33, 7, 163, 144, 145, 153, 154, 155,
      // RIGHT EYE
      362, 398, 384, 385, 386, 387, 388, 466, 263, 249, 390, 373, 374, 380, 381, 382,
    ];
  }

  addCamera() {
    this.camera = new THREE.OrthographicCamera(
      this.halfW,
      -this.halfW,
      -this.halfH,
      this.halfH,
      1,
      800,
    );
    this.camera.position.x = 320;
    this.camera.position.y = 240;
    this.camera.position.z = -600;
    this.camera.lookAt(320, 240, 0);
  }

  addLights() {
    const light = new THREE.HemisphereLight(0xffffff, 0xffffff, 0.2);
    this.scene.add(light);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(this.halfW, this.halfH * 0.5, -1000).normalize();
    this.scene.add(directionalLight);
  }

  addGeometry() {
    this.geometry = new THREE.BufferGeometry();
    this.geometry.setIndex(TRIANGULATION);
    this.geometry.setAttribute('position', new THREE.Float32BufferAttribute(positionBufferData, 3));
    this.geometry.setAttribute('uv', new THREE.Float32BufferAttribute(uvs, 2));
    this.geometry.computeVertexNormals();
  }

  addMaterial() {
    this.textureLoader = new THREE.TextureLoader().setCrossOrigin('anonymous');
    // this.textureLoader.setRequestHeader({
    //   'Access-Control-Allow-Origin': '*',
    // });
    const texture = this.textureLoader.load(
      `${this.textureFilePath}?not-from-cache-please`,
      (loadedTexture: THREE.Texture) => {
        console.log('텍스처 로딩 완료:', loadedTexture);
        console.log('텍스처 이미지:', loadedTexture.image); // 이미지 확인
      },
      undefined,
      (error: any) => {
        console.error('텍스처 로딩 에러:', error);
      },
    );
    console.log('텍스쳐: ', texture);
    texture.encoding = THREE.sRGBEncoding;

    texture.anisotropy = 16;
    const alpha = 0.4;
    const beta = 0.5;
    this.material = new THREE.MeshPhongMaterial({
      map: texture,
      color: new THREE.Color(0xffffff),
      specular: new THREE.Color(beta * 0.2, beta * 0.2, beta * 0.2),
      reflectivity: beta,
      shininess: 2 ** (alpha * 10),
    });
  }

  setupScene() {
    this.scene = new THREE.Scene();
    this.addCamera();
    this.addLights();
    this.addGeometry();
    this.addMaterial();
    this.mesh = new THREE.Mesh(this.geometry, this.material);
    this.scene.add(this.mesh);
  }

  render(positionBufferData: any) {
    this.geometry.setAttribute('position', new THREE.Float32BufferAttribute(positionBufferData, 3));
    this.geometry.attributes.position.needsUpdate = true;

    this.renderer.render(this.scene, this.camera);
  }

  constructor({ canvas, textureFilePath, w, h }: any) {
    this.renderer = new THREE.WebGLRenderer({
      antialias: true,
      alpha: true,
      canvas,
    });
    this.renderer.setSize(w, h);
    this.halfW = w * 0.3;
    this.halfH = h * 0.3;
    this.textureFilePath = textureFilePath;
    this.setupScene();
  }
}
