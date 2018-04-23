
export interface ApkView {
  id: number;
  name: string;
  description: string;
  category: string;
  image128?: string;
  image512?: any;
  dev_name: string;
  downloads: number;
  rating: number;
  time_uploads: Date;
}
